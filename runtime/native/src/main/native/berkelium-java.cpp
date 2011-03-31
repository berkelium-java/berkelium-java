#include "berkelium/Berkelium.hpp"
#include "berkelium/Context.hpp"
#include "berkelium/Window.hpp"
#include "berkelium/WindowDelegate.hpp"
#include "berkelium/ScriptUtil.hpp"

#include <cstring>
#include <string>
#include <iostream>

#include <jni.h>

class Berkelium_Java_Env {
	private:
		static JNIEnv* globalEnv;
		JNIEnv* oldEnv;
		
	public:
		Berkelium_Java_Env(JNIEnv* newEnv) {
			oldEnv = globalEnv;
			globalEnv = newEnv;
		}
		
		~Berkelium_Java_Env() {
			globalEnv = oldEnv;
		}
		
		static JNIEnv* get() {
			return globalEnv;
		}
};

JNIEnv* Berkelium_Java_Env::globalEnv = 0;

static inline void* getHandle(jobject self)
{
	JNIEnv* env = Berkelium_Java_Env::get();
	jclass c = env->GetObjectClass(self);
	jfieldID fid = env->GetFieldID(c, "handle", "J");
	return (void*)env->GetLongField(self, fid);
}

void Berkelium_Java_Registry_add(jlong handle, jobject obj)
{
	JNIEnv* env = Berkelium_Java_Env::get();
	jclass cls = env->FindClass("org/berkelium/java/Platform");
	jmethodID meth = env->GetStaticMethodID(cls, "add", "(JLjava/lang/Object;)V");
	env->CallStaticVoidMethod(cls, meth, handle, obj);
}

void Berkelium_Java_Registry_remove(jlong handle)
{
	JNIEnv* env = Berkelium_Java_Env::get();
	jclass cls = env->FindClass("org/berkelium/java/Platform");
	jmethodID meth = env->GetStaticMethodID(cls, "remove", "(J)V");
	env->CallStaticVoidMethod(cls, meth, handle);
}

jobject Berkelium_Java_Registry_get(jlong handle)
{
	JNIEnv* env = Berkelium_Java_Env::get();
	jclass cls = env->FindClass("org/berkelium/java/Platform");
	jmethodID meth = env->GetStaticMethodID(cls, "get", "(J)Ljava/lang/Object;");
	return env->CallStaticObjectMethod(cls, meth, handle);
}

static inline Berkelium::Window* getWindow(jobject self)
{
	return (Berkelium::Window*)getHandle(self);
}

#include "BufferImpl.cpp"
#include "Context.cpp"
#include "javawindowdelegateproxy.cpp"
#include "Platform.cpp"
#include "WindowImpl.cpp"
