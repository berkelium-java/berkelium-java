#include <org_berkelium_java_impl_Platform.h>

#if defined(WIN32) || defined(WIN64)
#include <windows.h>
#else
#include <stdlib.h>
#endif

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_Platform__1init(JNIEnv* env, jclass, jstring path)
{
	Berkelium_Java_Env jEnv(env);
	jboolean iscopy;
	const char* cPath = (const char*)env->GetStringUTFChars(path, &iscopy);
#if defined(WIN32) || defined(WIN64)
	SetEnvironmentVariable("PATH", cPath);
#else
	setenv("PATH", cPath, 1);
	//chdir(cPath);
#endif 
	env->ReleaseStringUTFChars(path, cPath);
	Berkelium::init(Berkelium::FileString::empty());
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_Platform_destroy(JNIEnv* env, jobject)
{
	Berkelium_Java_Env jEnv(env);
	Berkelium::destroy();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_Platform_update(JNIEnv* env, jobject)
{
	Berkelium_Java_Env jEnv(env);
	Berkelium::update();
}
