#if WIN32
#include <windows.h>
#ifndef CP_UTF8
#define CP_UTF8 65001
#endif
#endif

class JavaWindowDelegateProxy : public Berkelium::WindowDelegate {
private:
	jobject globalDelegate;
	
public:
	JavaWindowDelegateProxy(jobject localDelegate) {
		globalDelegate = Berkelium_Java_Env::get()->NewGlobalRef(localDelegate);
	}
	
	virtual void onPaint(
		Berkelium::Window *wini,
		const unsigned char *bitmap_in,
		const Berkelium::Rect &bitmap_rect,
		size_t num_copy_rects,
		const Berkelium::Rect *copy_rects,
		int dx,
		int dy,
		const Berkelium::Rect &scroll_rect)
	{
		callFuncA(
			"onPaint",
			"(Lorg/berkelium/java/api/Window;Lorg/berkelium/java/api/Buffer;Lorg/berkelium/java/api/Rect;[Lorg/berkelium/java/api/Rect;IILorg/berkelium/java/api/Rect;)V",
			map(wini),
			map(bitmap_in, 4 * bitmap_rect.width() * bitmap_rect.height()),
			map(bitmap_rect),
			map(num_copy_rects, copy_rects),
			dx,
			dy,
			map(scroll_rect)
		);
	}

	virtual void onAddressBarChanged(Berkelium::Window *win, Berkelium::URLString newURL) {
		callFunc("onAddressBarChanged", win, map(newURL));
	}
	virtual void onStartLoading(Berkelium::Window *win, Berkelium::URLString newURL) {
		callFunc("onStartLoading", win, map(newURL));
	}
	
	virtual void onLoad(Berkelium::Window *win) {
		callFunc("onLoad", win);
	}
	
	virtual void onCrashedWorker(Berkelium::Window *win) {
		callFunc("onCrashedWorker", win);
	}

	virtual void onCrashedPlugin(Berkelium::Window *win, Berkelium::WideString pluginName) {
		callFunc("onCrashedPlugin", win, map(pluginName));
	}

	virtual void onProvisionalLoadError(Berkelium::Window *win, Berkelium::URLString url,
										int errorCode, bool isMainFrame) {
		callFunc("onProvisionalLoadError", win, map(url), errorCode, isMainFrame);
	}

	virtual void onConsoleMessage(Berkelium::Window *win, Berkelium::WideString message,
								  Berkelium::WideString sourceId, int line_no) {
		callFunc("onConsoleMessage", win, map(message), map(sourceId), line_no);
	}

	virtual void onScriptAlert(Berkelium::Window *win, Berkelium::WideString message,
							  Berkelium::WideString defaultValue, Berkelium::URLString url,
							  int flags, bool &success, Berkelium::WideString &value) {
		callFuncA(
			"onScriptAlert",
			"(Lorg/berkelium/java/api/Window;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I[Z[Ljava/lang/String;)V",
			map(win),
			map(message),
			map(defaultValue),
			map(url),
			flags,
			mapRw(success),
			mapRw(value)
		);
		// FIXME: unmap(success)
		// FIXME: unmap(value)
	}

	virtual void onNavigationRequested(Berkelium::Window *win, Berkelium::URLString newURL,
									   Berkelium::URLString referrer, bool isNewWindow,
									   bool &cancelDefaultAction) {
		callFuncA(
			"onNavigationRequested",
			"(Lorg/berkelium/java/api/Window;Ljava/lang/String;Ljava/lang/String;Z[Z)Z",
			map(win),
			map(newURL),
			map(referrer),
			isNewWindow,
			mapRw(cancelDefaultAction)
		);
		// FIXME: unmap(cancelDefaultAction)
	}

	virtual void onLoadingStateChanged(Berkelium::Window *win, bool isLoading) {
		callFunc("onLoadingStateChanged", win, isLoading);
	}

	virtual void onTitleChanged(Berkelium::Window *win, Berkelium::WideString title) {
		callFunc("onTitleChanged", win, map(title));
	}

	virtual void onTooltipChanged(Berkelium::Window *win, Berkelium::WideString text) {
		callFunc("onTooltipChanged", win, map(text));
	}

	virtual void onCrashed(Berkelium::Window *win) {
		callFunc("onCrashed", win);
	}

	virtual void onUnresponsive(Berkelium::Window *win) {
		callFunc("onUnresponsive", win);
	}

	virtual void onResponsive(Berkelium::Window *win) {
		callFunc("onResponsive", win);
	}

	virtual void onCreatedWindow(Berkelium::Window *win, Berkelium::Window *newWindow,
								 const Berkelium::Rect &initialRect) {
		// FIXME: move setDelegate call to java
		newWindow->setDelegate(this);
		callFunc("onCreatedWindow", win, newWindow, initialRect);
	}

	virtual void onWidgetCreated(Berkelium::Window *win, Berkelium::Widget *newWidget, int zIndex) {
		/*
		callFunc("onWidgetCreated", win, newWidget, zIndex);
		*/
	}

	virtual void onWidgetResize(Berkelium::Window *win, Berkelium::Widget *wid, int newWidth, int newHeight) {
		/*
		callFunc("onWidgetResize", win, wid, newWidth, newHeight);
		*/
	}

	virtual void onWidgetMove(Berkelium::Window *win, Berkelium::Widget *wid, int newX, int newY) {
		/*
		callFunc("onWidgetMove", win, wid, newX, newY);
		*/
	}

	virtual void onShowContextMenu(Berkelium::Window *win,
								   const Berkelium::ContextMenuEventArgs& args) {
		std::cout << "FIXME, NOT IMPLEMENTED: onShowContextMenu at " << args.mouseX << "," << args.mouseY;
		std::cout << std::endl;
	}

	virtual void onJavascriptCallback(Berkelium::Window *win, void* replyMsg, Berkelium::URLString url, Berkelium::WideString funcName, Berkelium::Script::Variant *args, size_t numArgs) {
		callFunc("onJavascriptCallback", win, map(url), map(funcName));
		/*
		std::cout << "FIXME, NOT IMPLEMENTED: onJavaBerkelium::ScriptCallback at URL " << url << ", "
				  << (replyMsg?"synchronous":"async") << std::endl;
		std::wcout << L"	Function name: " << funcName << std::endl;
		*/
		for (size_t i = 0; i < numArgs; i++) {
			Berkelium::WideString jsonStr = toJSON(args[i]);
			std::wcout << L"	Argument " << i << ": ";
			if (args[i].type() == Berkelium::Script::Variant::JSSTRING) {
				std::wcout << L"(string) " << args[i].toString() << std::endl;
			} else {
				std::wcout << jsonStr << std::endl;
			}
			Berkelium::Script::toJSON_free(jsonStr);
		}
		if (replyMsg) {
			win->synchronousScriptReturn(replyMsg, numArgs ? args[0] : Berkelium::Script::Variant());
		}
	}

	/** Display a file chooser dialog, if necessary. The value to be returned should go ______.
	 * \param win  Window instance that fired this event.
	 * \param mode  Type of file chooser expected. See FileChooserType.
	 * \param title  Title for dialog. "Open" or "Save" should be used if empty.
	 * \param defaultFile  Default file to select in dialog.
	 */
	virtual void onRunFileChooser(Berkelium::Window *win, int mode, Berkelium::WideString title, Berkelium::FileString defaultFile) {
		std::wcout << L"FIXME, NOT IMPLEMENTED: onRunFileChooser type " << mode << L", title " << title << L":" << std::endl;
#ifdef _WIN32
		std::wcout <<
#else
		std::cout <<
#endif
			defaultFile << std::endl;
	
		win->filesSelected(NULL);
	}

	virtual void onExternalHost(
		Berkelium::Window *win,
		Berkelium::WideString message,
		Berkelium::URLString origin,
		Berkelium::URLString target)
	{
		callFunc("onExternalHost", win, map(message), map(origin), map(target));
	}

private:

	void callFunc(const char* func, Berkelium::Window *win) {
		callFuncA(func, "(Lorg/berkelium/java/api/Window;)V", map(win));
	}

	void callFunc(const char* func, Berkelium::Window *win, bool val) {
		callFuncA(func, "(Lorg/berkelium/java/api/Window;Z)V", map(win), val);
	}

	void callFunc(const char* func, Berkelium::Window *win, jstring str) {
		callFuncA(func, "(Lorg/berkelium/java/api/Window;Ljava/lang/String;)V", map(win), str);
	}

	void callFunc(const char* func, Berkelium::Window *win, jstring arg1, jstring arg2){
		callFuncA(func, "(Lorg/berkelium/java/api/Window;Ljava/lang/String;Ljava/lang/String;)V", map(win), arg1, arg2);
	}

	void callFunc(const char* func, Berkelium::Window *win, jstring arg1, jstring arg2, jstring arg3){
		callFuncA(func, "(Lorg/berkelium/java/api/Window;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", map(win), arg1, arg2, arg3);
	}

	void callFunc(const char* func, Berkelium::Window *win, jstring arg1, jstring arg2, int arg3) {
		callFuncA(func, "(Lorg/berkelium/java/api/Window;Ljava/lang/String;Ljava/lang/String;I)V", map(win), arg1, arg2, arg3);
	}

	void callFunc(const char* func, Berkelium::Window *win, jstring arg1, int arg2, bool arg3) {
		callFuncA(func, "(Lorg/berkelium/java/api/Window;Ljava/lang/String;IZ)V", map(win), arg1, arg2, arg3);
	}

	void callFunc(const char* func, Berkelium::Window* win, Berkelium::Window* arg1, const Berkelium::Rect& arg2) {
		callFuncA(func, "(Lorg/berkelium/java/api/Window;Lorg/berkelium/java/api/Window;Lorg/berkelium/java/api/Rect;)V", map(win), map(arg1), map(arg2));
	}
/*
	void callFunc(const char* func, Berkelium::Window* win, Berkelium::Widget* arg1, int arg2) {
		callFuncA(func, "(Lorg/berkelium/java/api/Window;Lorg/berkelium/java/api/Widget;I)V", map(win), map(arg1), arg2);
	}

	void callFunc(const char* func, Berkelium::Window* win, Berkelium::Widget* arg1, int arg2, int arg3) {
		callFuncA(func, "(Lorg/berkelium/java/api/Window;Lorg/berkelium/java/api/Widget;II)V", map(win), map(arg1), arg2, arg3);
	}
*/
	void callFuncA(const char* func, const char* sig, ...) {
		va_list args;
		JNIEnv* env = Berkelium_Java_Env::get();
		jobject obj = env->NewGlobalRef(globalDelegate);
		if (obj == 0) {
			return;
		}
		jclass cls = env->GetObjectClass(obj);
		if (cls != 0) {
			jmethodID jmid = env->GetMethodID(cls, func, sig);
			if (jmid != 0) {
				va_start(args, sig);
				env->CallVoidMethodV(obj, jmid, args);
				va_end(args);
			}
		}
		env->DeleteGlobalRef(obj);
	}

	jobject map(Berkelium::Window* win) {
		if(win == NULL) return NULL;
		return Berkelium_Java_Registry_get((jlong)win);
	}

	jobject map(Berkelium::Widget* wid) {
		if(wid == NULL) return NULL;
		return Berkelium_Java_Registry_get((jlong)wid);
	}

	jobject map(const Berkelium::Rect& in) {
		JNIEnv* env = Berkelium_Java_Env::get();
		jclass cls = env->FindClass("org/berkelium/java/impl/Platform");
		if (cls == 0)return 0;
		jmethodID meth = env->GetStaticMethodID(cls, "createRect", "(IIII)Lorg/berkelium/java/api/Rect;");
		if (meth == 0)return 0;
		return env->CallStaticObjectMethod(cls, meth, in.x(), in.y(), in.width(), in.height());
	}

	jstring map(Berkelium::URLString str) {
		if(str.data() == NULL) return NULL;
		return Berkelium_Java_Env::get()->NewStringUTF(str.data());
	}

	jobject map(const void* data, size_t num) {
		JNIEnv* env = Berkelium_Java_Env::get();
		jclass cls = env->FindClass("org/berkelium/java/impl/BufferImpl");
		if (cls == 0)return 0;
		jmethodID meth = env->GetStaticMethodID(cls, "create", "(JI)Ljava/lang/Object;");
		if (meth == 0)return 0;
		return env->CallStaticObjectMethod(cls, meth, (jlong)data, (jsize)num);
	}

	jobject map(size_t num, const Berkelium::Rect* rects) {
		JNIEnv* env = Berkelium_Java_Env::get();
		jclass cls = env->FindClass("org/berkelium/java/impl/Platform");
		if (cls == 0)return 0;
		jmethodID meth = env->GetStaticMethodID(cls, "createRectArray", "(I)Ljava/lang/Object;");
		if (meth == 0)return 0;
		jobject ret = env->CallStaticObjectMethod(cls, meth, num);
		if (ret == 0)return 0;
		meth = env->GetStaticMethodID(cls, "createRectInArray", "(Ljava/lang/Object;IIIII)V");
		if (meth == 0)return 0;
		for (size_t i = 0; i < num; ++i) {
			const Berkelium::Rect& in = rects[i];
			env->CallStaticVoidMethod(cls, meth, ret, i, in.x(), in.y(), in.width(), in.height());
		}
		return ret;
	}

	jbooleanArray mapRw(bool& val) {
		// FIXME!
		return 0;
	}

	jbooleanArray mapRw(Berkelium::WideString& val) {
		// FIXME!
		return 0;
	}

	jstring map(const Berkelium::WideString& ws) {
		if(ws.data() == NULL) return NULL;
		return wideString2jstring(ws);
	}

};
