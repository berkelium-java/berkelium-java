#ifndef __BERKELIUM_JAVA_H__
#define __BERKELIUM_JAVA_H__

#include "berkelium/Berkelium.hpp"
#include "berkelium/Context.hpp"
#include "berkelium/Window.hpp"
#include "berkelium/WindowDelegate.hpp"
#include "berkelium/ScriptUtil.hpp"
#include "berkelium/ScriptVariant.hpp"
#include "berkelium/StringUtil.hpp"

#include <cstring>
#include <string>
#include <iostream>
#if defined(WIN32) || defined(WIN64)
#include <windows.h>
#ifndef CP_UTF8
#define CP_UTF8 65001
#endif
#else
#include <stdlib.h>
#endif

#include <jni.h>

#include <org_berkelium_java_impl_Context.h>
#include <org_berkelium_java_impl_WindowImpl.h>
#include <org_berkelium_java_impl_SingleThreadBerkelium.h>

class Java {
public:
	JNIEnv* env;
	jobject thread;
	Berkelium::WeakString<char> threadName;

	// ctor
	Java(JNIEnv* env);

	// access
	static Java* getJava();
	static Java* getOrCreateJava(JNIEnv* env);

	// object creation
	jobject NewInteger(jint value);
	jobject NewGlobalInteger(jint value);

	// string conversation
	void* getHandle(jobject self);
	Berkelium::WeakString<char> jstring2String(jstring string);
	Berkelium::WideString jstring2WideString(jstring string);
	jstring utf8String2jstring(const Berkelium::UTF8String& string);
	jstring wideString2jstring(const Berkelium::WideString& string);

	// logging
	void log(const char* msg);
	void fail(const char* msg);

	// Berkelium Java Registry
	void add(jlong handle, jobject obj);
	void remove(jlong handle);
	jobject get(jlong handle, jint type);
	Berkelium::Window* getWindow(jobject self);

	jobject map(Berkelium::Window* win);
	jobject map(Berkelium::Widget* wid);
	jstring map(Berkelium::URLString str);
	jobject map(const Berkelium::Rect& in);
	jobject map(const void* data, size_t num);
	jstring map(const Berkelium::WideString& ws);
	jobject map(size_t num, const Berkelium::Rect* rects);
	jbooleanArray mapRw(bool& val);
	jbooleanArray mapRw(Berkelium::WideString& val);

	// classes and methods
	struct {
		jclass Integer;
		jclass Thread;
		jclass RuntimeException;
		jclass SingleThreadBerkelium;
		jclass WindowDelegate;
	} _class;
	struct {
		jmethodID Thread_currentThread;
		jmethodID SingleThreadBerkelium_add;
		jmethodID SingleThreadBerkelium_remove;
		jmethodID SingleThreadBerkelium_get;
	} _static;
	struct {
		jmethodID Thread_getName;
		jmethodID WindowDelegate_onPaint;
		jmethodID WindowDelegate_onResize;
		jmethodID WindowDelegate_onAddressBarChanged;
		jmethodID WindowDelegate_onStartLoading;
		jmethodID WindowDelegate_onLoad;
		jmethodID WindowDelegate_onCrashedWorker;
		jmethodID WindowDelegate_onCrashedPlugin;
		jmethodID WindowDelegate_onProvisionalLoadError;
		jmethodID WindowDelegate_onConsoleMessage;
		jmethodID WindowDelegate_onScriptAlert;
		jmethodID WindowDelegate_freeLastScriptAlert;
		jmethodID WindowDelegate_onNavigationRequested;
		jmethodID WindowDelegate_onLoadingStateChanged;
		jmethodID WindowDelegate_onTitleChanged;
		jmethodID WindowDelegate_onTooltipChanged;
		jmethodID WindowDelegate_onCrashed;
		jmethodID WindowDelegate_onUnresponsive;
		jmethodID WindowDelegate_onResponsive;
		jmethodID WindowDelegate_onExternalHost;
		jmethodID WindowDelegate_onCreatedWindow;
		jmethodID WindowDelegate_onJavascriptCallback;
		jmethodID WindowDelegate_onRunFileChooser;
	} _call;
};

#endif
