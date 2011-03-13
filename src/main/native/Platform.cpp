#include <org_berkelium_Platform.h>

JNIEXPORT void JNICALL Java_org_berkelium_Platform__1init(JNIEnv *, jobject)
{
	Berkelium::init(Berkelium::FileString::empty());
}

JNIEXPORT void JNICALL Java_org_berkelium_Platform_destroy(JNIEnv *, jobject)
{
	Berkelium::destroy();
}

static JNIEnv* Berkelium_Java_Update_JNIEnv;

JNIEXPORT void JNICALL Java_org_berkelium_Platform_update(JNIEnv* env, jobject)
{
	JNIEnv* old = Berkelium_Java_Update_JNIEnv;
	Berkelium_Java_Update_JNIEnv = env;
	Berkelium::update();
	Berkelium_Java_Update_JNIEnv = old;
}
