#include <org_berkelium_Platform.h>

#if WIN32
#include <windows.h>
#endif

JNIEXPORT void JNICALL Java_org_berkelium_Platform__1init(JNIEnv* env, jclass, jstring path)
{
	Berkelium_Java_Env jEnv(env);
#if WIN32
	jboolean iscopy;
	const char* cPath = (const char*)env->GetStringUTFChars(path, &iscopy);
	SetEnvironmentVariable("PATH", cPath);
	env->ReleaseStringUTFChars(path, cPath);
#endif 
	Berkelium::init(Berkelium::FileString::empty());
}

JNIEXPORT void JNICALL Java_org_berkelium_Platform_destroy(JNIEnv* env, jobject)
{
	Berkelium_Java_Env jEnv(env);
	Berkelium::destroy();
}

JNIEXPORT void JNICALL Java_org_berkelium_Platform_update(JNIEnv* env, jobject)
{
	Berkelium_Java_Env jEnv(env);
	Berkelium::update();
}
