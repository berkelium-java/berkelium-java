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

static inline Berkelium::WideString jstring2WideString(jstring string)
{
	JNIEnv* env = Berkelium_Java_Env::get();
	jboolean iscopy;
	const char* data = env->GetStringUTFChars(string, &iscopy);
	jint len = env->GetStringUTFLength(string);
	char* copy = new char[len];
	std::memcpy(copy, data, len);
	env->ReleaseStringUTFChars(string, data);
	return Berkelium::UTF8ToWide(Berkelium::WeakString<char>::point_to(copy, len));
}

static inline jstring wideString2jstring(const Berkelium::WideString& string)
{
	JNIEnv* env = Berkelium_Java_Env::get();
	char* copy;
	{
		Berkelium::UTF8String utf = Berkelium::WideToUTF8(string);
		int len = utf.length();
		copy = new char[len + 1];
		std::memcpy(copy, utf.data(), len);
		copy[len] = 0;
	}
	jstring ret = env->NewStringUTF(copy);
	delete[] copy;
	return ret;
}

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
