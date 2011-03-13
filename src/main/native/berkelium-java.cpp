#include "Berkelium/Berkelium.hpp"
#include "Berkelium/Context.hpp"
#include "Berkelium/Window.hpp"
#include "Berkelium/WindowDelegate.hpp"
#include "berkelium/ScriptUtil.hpp"

#include <cstring>
#include <string>
#include <iostream>

#include <jni.h>

static inline void* getHandle(JNIEnv* env, jobject self)
{
	jclass c = env->GetObjectClass(self);
	jfieldID fid = env->GetFieldID(c, "handle", "J");
	return (void*)env->GetLongField(self, fid);
}

void Berkelium_Java_Registry_add(JNIEnv* env, jlong handle, jobject obj)
{
	jclass cls = env->FindClass("org/berkelium/Platform");
	jmethodID meth = env->GetStaticMethodID(cls, "add", "(JLjava/lang/Object;)V");
	env->CallStaticVoidMethod(cls, meth, handle, obj);
}

void Berkelium_Java_Registry_remove(JNIEnv* env, jlong handle)
{
	jclass cls = env->FindClass("org/berkelium/Platform");
	jmethodID meth = env->GetStaticMethodID(cls, "remove", "(J)V");
	env->CallStaticVoidMethod(cls, meth, handle);
}

jobject Berkelium_Java_Registry_get(JNIEnv* env, jlong handle)
{
	jclass cls = env->FindClass("org/berkelium/Platform");
	jmethodID meth = env->GetStaticMethodID(cls, "get", "(J)Ljava/lang/Object;");
	return env->CallStaticObjectMethod(cls, meth, handle);
}

jobject Berkelium_Java_Rect(JNIEnv* env, const Berkelium::Rect &in)
{
	jclass cls = env->FindClass("org/berkelium/Rect");
	jmethodID meth = env->GetStaticMethodID(cls, "createRect", "(IIII)Lorg/berkelium/Rect;");
	return env->CallStaticObjectMethod(cls, meth, in.x(), in.y(), in.width(), in.height());
}

jobject Berkelium_Java_Rects(JNIEnv* env, size_t num, const Berkelium::Rect* rects)
{
	jclass cls = env->FindClass("org/berkelium/Rect");
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

jobject Berkelium_Java_Buffer(JNIEnv* env, const void* data, size_t num)
{
	jclass cls = env->FindClass("org/berkelium/Buffer");
	if (cls == 0)return 0;
	jmethodID meth = env->GetStaticMethodID(cls, "create", "(JI)Ljava/lang/Object;");
	if (meth == 0)return 0;
	return env->CallStaticObjectMethod(cls, meth, (jlong)data, (jsize)num);
}

static inline Berkelium::Window* getWindow(JNIEnv* env, jobject self)
{
	return (Berkelium::Window*)getHandle(env, self);
}

#include "BufferImpl.cpp"
#include "Context.cpp"
#include "javawindowdelegateproxy.cpp"
#include "Platform.cpp"
#include "WindowImpl.cpp"
