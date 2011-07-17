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

static inline Berkelium::WeakString<char> jstring2String(jstring string)
{
	if(string == NULL) {
		return Berkelium::WeakString<char>::point_to((const char*)NULL, 0);
	}
	JNIEnv* env = Berkelium_Java_Env::get();
	jboolean iscopy;
	const char* data = env->GetStringUTFChars(string, &iscopy);
	jint len = env->GetStringUTFLength(string);
	char* copy = new char[len];
	std::memcpy(copy, data, len);
	env->ReleaseStringUTFChars(string, data);
	return Berkelium::WeakString<char>::point_to(copy, len);
}

static inline Berkelium::WideString jstring2WideString(jstring string)
{
	if(string == NULL) {
		return Berkelium::WideString::point_to((const wchar_t*)NULL, 0);
	}
	return Berkelium::UTF8ToWide(jstring2String(string));
}

static inline jstring utf8String2jstring(const Berkelium::UTF8String& string)
{
	if(string.data() == NULL) {
		return NULL;
	}

	JNIEnv* env = Berkelium_Java_Env::get();
	int l = string.length();
	char* tmp = new char[l + 1];
	memcpy(tmp, string.data(), string.length());
	tmp[l] = 0;
	jstring ret = env->NewStringUTF(tmp);
	delete[] tmp;
	return ret;
}

static inline jstring wideString2jstring(const Berkelium::WideString& string)
{
	if(string.data() == NULL) {
		return NULL;
	}
	return utf8String2jstring(Berkelium::WideToUTF8(string));
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
	jclass cls = env->FindClass("org/berkelium/java/impl/SingleThreadBerkelium");
	jmethodID meth = env->GetStaticMethodID(cls, "add", "(JLjava/lang/Object;)V");
	env->CallStaticVoidMethod(cls, meth, handle, obj);
}

void Berkelium_Java_Registry_remove(jlong handle)
{
	JNIEnv* env = Berkelium_Java_Env::get();
	jclass cls = env->FindClass("org/berkelium/java/impl/SingleThreadBerkelium");
	jmethodID meth = env->GetStaticMethodID(cls, "remove", "(J)V");
	env->CallStaticVoidMethod(cls, meth, handle);
}

jobject Berkelium_Java_Registry_get(jlong handle)
{
	JNIEnv* env = Berkelium_Java_Env::get();
	jclass cls = env->FindClass("org/berkelium/java/impl/SingleThreadBerkelium");
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
#include "SingleThreadBerkelium.cpp"
#include "WindowImpl.cpp"
