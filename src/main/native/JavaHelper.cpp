#include "berkelium-java.h"

Java::Java(JNIEnv* e) {
	env = e;

	_class.Integer = e->FindClass("java/lang/Integer");
	_class.Thread = e->FindClass("java/lang/Thread");
	_class.RuntimeException = e->FindClass("java/lang/RuntimeException");
	_class.SingleThreadBerkelium = e->FindClass("org/berkelium/java/impl/SingleThreadBerkelium");
	_class.WindowDelegate = e->FindClass("org/berkelium/java/api/WindowDelegate");

	_static.SingleThreadBerkelium_add = e->GetStaticMethodID(_class.SingleThreadBerkelium, "add", "(JLjava/lang/Object;)V");
	_static.SingleThreadBerkelium_remove = e->GetStaticMethodID(_class.SingleThreadBerkelium, "remove", "(J)V");
	_static.SingleThreadBerkelium_get = e->GetStaticMethodID(_class.SingleThreadBerkelium, "get", "(JI)Ljava/lang/Object;");
	_static.Thread_currentThread = e->GetStaticMethodID(_class.Thread, "currentThread", "()Ljava/lang/Thread;");

	const char* windowVoid = "(Lorg/berkelium/java/api/Window;)V";
	const char* windowStringVoid = "(Lorg/berkelium/java/api/Window;Ljava/lang/String;)V";
	_call.Thread_getName = e->GetMethodID(_class.Thread, "getName", "()Ljava/lang/String;");
	_call.WindowDelegate_onPaint = e->GetMethodID(_class.WindowDelegate, "onPaint", "(Lorg/berkelium/java/api/Window;Lorg/berkelium/java/api/Buffer;Lorg/berkelium/java/api/Rect;[Lorg/berkelium/java/api/Rect;IILorg/berkelium/java/api/Rect;)V");
	_call.WindowDelegate_onResize = e->GetMethodID(_class.WindowDelegate, "onResize", "(II)V");
	_call.WindowDelegate_onAddressBarChanged = e->GetMethodID(_class.WindowDelegate, "onAddressBarChanged", windowStringVoid);
	_call.WindowDelegate_onStartLoading = e->GetMethodID(_class.WindowDelegate, "onStartLoading", windowStringVoid);
	_call.WindowDelegate_onLoad = e->GetMethodID(_class.WindowDelegate, "onLoad", windowVoid);
	_call.WindowDelegate_onCrashedWorker = e->GetMethodID(_class.WindowDelegate, "onCrashedWorker", windowVoid);
	_call.WindowDelegate_onCrashedPlugin = e->GetMethodID(_class.WindowDelegate, "onCrashedPlugin", windowStringVoid);
	_call.WindowDelegate_onProvisionalLoadError = e->GetMethodID(_class.WindowDelegate, "onProvisionalLoadError", "(Lorg/berkelium/java/api/Window;Ljava/lang/String;IZ)V");
	_call.WindowDelegate_onConsoleMessage = e->GetMethodID(_class.WindowDelegate, "onConsoleMessage", "(Lorg/berkelium/java/api/Window;Ljava/lang/String;Ljava/lang/String;I)V");
	_call.WindowDelegate_onScriptAlert = e->GetMethodID(_class.WindowDelegate, "onScriptAlert", "(Lorg/berkelium/java/api/Window;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I[Z[Ljava/lang/String;)V");
	_call.WindowDelegate_freeLastScriptAlert = e->GetMethodID(_class.WindowDelegate, "freeLastScriptAlert", "(Ljava/lang/String;)V");
	_call.WindowDelegate_onNavigationRequested = e->GetMethodID(_class.WindowDelegate, "onNavigationRequested", "(Lorg/berkelium/java/api/Window;Ljava/lang/String;Ljava/lang/String;Z[Z)Z");
	_call.WindowDelegate_onLoadingStateChanged = e->GetMethodID(_class.WindowDelegate, "onLoadingStateChanged", "(Lorg/berkelium/java/api/Window;Z)V");
	_call.WindowDelegate_onTitleChanged = e->GetMethodID(_class.WindowDelegate, "onTitleChanged", windowStringVoid);
	_call.WindowDelegate_onTooltipChanged = e->GetMethodID(_class.WindowDelegate, "onTooltipChanged", windowStringVoid);
	_call.WindowDelegate_onCrashed = e->GetMethodID(_class.WindowDelegate, "onCrashed", windowVoid);
	_call.WindowDelegate_onUnresponsive = e->GetMethodID(_class.WindowDelegate, "onUnresponsive", windowVoid);
	_call.WindowDelegate_onResponsive = e->GetMethodID(_class.WindowDelegate, "onResponsive", windowVoid);
	_call.WindowDelegate_onExternalHost = e->GetMethodID(_class.WindowDelegate, "onExternalHost", "(Lorg/berkelium/java/api/Window;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
	_call.WindowDelegate_onCreatedWindow = e->GetMethodID(_class.WindowDelegate, "onCreatedWindow", "(Lorg/berkelium/java/api/Window;Lorg/berkelium/java/api/Window;Lorg/berkelium/java/api/Rect;)V");
	_call.WindowDelegate_onJavascriptCallback = e->GetMethodID(_class.WindowDelegate, "onJavascriptCallback", "(Lorg/berkelium/java/api/Window;Ljava/lang/String;Ljava/lang/String;)V");
	_call.WindowDelegate_onRunFileChooser = e->GetMethodID(_class.WindowDelegate, "onRunFileChooser", "(Lorg/berkelium/java/api/Window;ILjava/lang/String;Ljava/lang/String;)V");

	thread = env->CallStaticObjectMethod(_class.Thread, _static.Thread_currentThread);
	threadName = jstring2String((jstring)env->CallObjectMethod(thread, _call.Thread_getName));
}

jobject Java::NewInteger(jint value) {
    jclass cls = env->FindClass("java/lang/Integer");
    jmethodID methodID = env->GetMethodID(cls, "<init>", "(I)V");
    return env->NewObject(cls, methodID, value);
}

jobject Java::NewGlobalInteger(jint value) {
    return env->NewGlobalRef(NewInteger(value));
}

void* Java::getHandle(jobject self) {
	jclass c = env->GetObjectClass(self);
	jfieldID fid = env->GetFieldID(c, "handle", "J");
	return (void*)env->GetLongField(self, fid);
}

Berkelium::Window* Java::getWindow(jobject self) {
	return (Berkelium::Window*)getHandle(self);
}

void Java::add(jlong handle, jobject obj) {
	env->CallStaticVoidMethod(_class.SingleThreadBerkelium, _static.SingleThreadBerkelium_add, handle, obj);
}

void Java::remove(jlong handle) {
	env->CallStaticVoidMethod(_class.SingleThreadBerkelium, _static.SingleThreadBerkelium_remove, handle);
}

jobject Java::get(jlong handle, jint type) {
	return env->CallStaticObjectMethod(_class.SingleThreadBerkelium, _static.SingleThreadBerkelium_get, handle, type);
}

jobject Java::map(Berkelium::Window* win) {
	if(win == NULL) return NULL;
	return get((jlong)win, (jint)1);
}

jobject Java::map(Berkelium::Widget* wid) {
	if(wid == NULL) return NULL;
	return get((jlong)wid, (jint)2);
}

jobject Java::map(const Berkelium::Rect& in) {
	jclass cls = env->FindClass("org/berkelium/java/impl/SingleThreadBerkelium");
	if (cls == 0)return 0;
	jmethodID meth = env->GetStaticMethodID(cls, "createRect", "(IIII)Lorg/berkelium/java/api/Rect;");
	if (meth == 0)return 0;
	return env->CallStaticObjectMethod(cls, meth, in.x(), in.y(), in.width(), in.height());
}

jstring Java::map(Berkelium::URLString str) {
	if(str.data() == NULL) return NULL;
	return env->NewStringUTF(str.data());
}

jobject Java::map(const void* data, size_t num) {
	jclass cls = env->FindClass("org/berkelium/java/impl/BufferImpl");
	if (cls == 0)return 0;
	jmethodID meth = env->GetStaticMethodID(cls, "create", "(JI)Ljava/lang/Object;");
	if (meth == 0)return 0;
	return env->CallStaticObjectMethod(cls, meth, (jlong)data, (jsize)num);
}

jobject Java::map(size_t num, const Berkelium::Rect* rects) {
	jclass cls = env->FindClass("org/berkelium/java/impl/SingleThreadBerkelium");
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

jbooleanArray Java::mapRw(bool& val) {
	// FIXME!
	return 0;
}

jbooleanArray Java::mapRw(Berkelium::WideString& val) {
	// FIXME!
	return 0;
}

jstring Java::map(const Berkelium::WideString& ws) {
	if(ws.data() == NULL) return NULL;
	return wideString2jstring(ws);
}

Berkelium::WeakString<char> Java::jstring2String(jstring string) {
	if(string == NULL) {
		return Berkelium::WeakString<char>::point_to((const char*)NULL, 0);
	}
	jboolean iscopy;
	const char* data = env->GetStringUTFChars(string, &iscopy);
	jint len = env->GetStringUTFLength(string);
	char* copy = new char[len + 1];
	std::memcpy(copy, data, len);
	env->ReleaseStringUTFChars(string, data);
	return Berkelium::WeakString<char>::point_to(copy, len);
}

Berkelium::WideString Java::jstring2WideString(jstring string) {
	if(string == NULL) {
		return Berkelium::WideString::point_to((const wchar_t*)NULL, 0);
	}
	return Berkelium::UTF8ToWide(jstring2String(string));
}

jstring Java::utf8String2jstring(const Berkelium::UTF8String& string) {
	if(string.data() == NULL) {
		return NULL;
	}

	int l = string.length();
	char* tmp = new char[l + 1];
	memcpy(tmp, string.data(), string.length());
	tmp[l] = 0;
	jstring ret = (jstring)env->NewGlobalRef(env->NewStringUTF(tmp));
	delete[] tmp;
	return ret;
}

jstring Java::wideString2jstring(const Berkelium::WideString& string) {
	if(string.data() == NULL) {
		return NULL;
	}
	return utf8String2jstring(Berkelium::WideToUTF8(string));
}

void Java::log(const char* msg) {
	fprintf(stderr, "Berkelium-Java '%s': %s\n", threadName.data(), msg);
}

void Java::fail(const char* msg) {
	log(msg);
	env->ThrowNew(_class.RuntimeException, msg);

}

#ifdef WIN32
static __declspec( thread ) Java* java;
#else
static __thread Java* java;
#endif
static Java* berkeliumThread = NULL;

const char* illegalThreadAccess = "Berkelium-Java: Illegal Thread Access";

Java* Java::getJava() {
	if(java == NULL) {
		fprintf(stderr, "%s!\n", illegalThreadAccess);
		return NULL;
	}
	return java;
}

Java* Java::getOrCreateJava(JNIEnv* env) {
	if(java == NULL) {
		if(berkeliumThread != NULL) {
			fprintf(stderr, "%s!\n", illegalThreadAccess);
			env->ThrowNew(env->FindClass("java/lang/IllegalThreadStateException"), illegalThreadAccess);
			return NULL;
		}
		berkeliumThread = java = new Java(env);
	}
	return java;
}
