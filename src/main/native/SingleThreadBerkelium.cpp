#include "berkelium-java.h"

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_SingleThreadBerkelium__1init(JNIEnv* env, jclass, jstring path, jstring berkeliumPath)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
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
	// doesn't compile with second argument!
	Berkelium::init(Berkelium::FileString::empty(), java->jstring2WideString(berkeliumPath));
	//Berkelium::init(Berkelium::FileString::empty());
#else
	// doesn't compile with second argument!
	Berkelium::init(Berkelium::FileString::empty(), java->jstring2String(berkeliumPath));
	//Berkelium::init(Berkelium::FileString::empty());
#endif
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_SingleThreadBerkelium_destroy(JNIEnv* env, jobject)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	Berkelium::destroy();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_SingleThreadBerkelium__1update(JNIEnv* env, jobject)
{
	Java* java = Java::getOrCreateJava(env);
	if(java == NULL) return;
	Berkelium::update();
}
