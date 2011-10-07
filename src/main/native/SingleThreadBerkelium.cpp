#include <org_berkelium_java_impl_SingleThreadBerkelium.h>

#if defined(WIN32) || defined(WIN64)
#include <windows.h>
#else
#include <stdlib.h>
#endif

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_SingleThreadBerkelium__1init(JNIEnv* env, jclass, jstring path, jstring berkeliumPath)
{
	Berkelium_Java_Env jEnv(env);
	jboolean iscopy;
	const char* cPath = (const char*)env->GetStringUTFChars(path, &iscopy);
#if defined(WIN32) || defined(WIN64)
	SetEnvironmentVariable("PATH", cPath);
#else
	setenv("PATH", cPath, 1);
	char *ld = getenv("LD_LIBRARY_PATH");
	if(ld == NULL) {
		setenv("LD_LIBRARY_PATH", ".", 1);
	} else {
		char *ld2 = (char *)malloc(strlen(ld) + strlen(cPath) + 1);
		sprintf(ld2, "%s:%s", ld, cPath);
		setenv("LD_LIBRARY_PATH", ld2, 1);
		free(ld2);
	}
	//chdir(cPath);
#endif 
	env->ReleaseStringUTFChars(path, cPath);
#if defined(WIN32) || defined(WIN64)
	Berkelium::init(Berkelium::FileString::empty(), jstring2WideString(berkeliumPath));
#else
	Berkelium::init(Berkelium::FileString::empty(), jstring2String(berkeliumPath));
#endif
}

//TODO private _destroy with thread access check
JNIEXPORT void JNICALL Java_org_berkelium_java_impl_SingleThreadBerkelium_destroy(JNIEnv* env, jobject)
{
	Berkelium_Java_Env jEnv(env);
	Berkelium::destroy();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_SingleThreadBerkelium__1update(JNIEnv* env, jobject)
{
	Berkelium_Java_Env jEnv(env);
	Berkelium::update();
}
