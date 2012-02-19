#include "berkelium-java.h"

JNIEXPORT jlong JNICALL Java_org_berkelium_java_impl_Context__1init(JNIEnv *, jclass)
{
	return (jlong)Berkelium::Context::create();
}

JNIEXPORT void JNICALL Java_org_berkelium_java_impl_Context__1destroy(JNIEnv *, jclass, jlong handle)
{
	((Berkelium::Context*)handle)->destroy();
}
