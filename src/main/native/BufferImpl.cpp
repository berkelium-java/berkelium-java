#include <org_berkelium_java_impl_BufferImpl.h>

JNIEXPORT jintArray JNICALL Java_org_berkelium_java_impl_BufferImpl_createIntArray
  (JNIEnv* env, jclass, jlong data, jsize size){
	size /= sizeof(jint);
	jintArray ret =  env->NewIntArray(size);
	env->SetIntArrayRegion(ret, 0, size, (jint*)data);
	return ret;
}

JNIEXPORT jbyteArray JNICALL Java_org_berkelium_java_impl_BufferImpl_createByteArray
  (JNIEnv* env, jclass, jlong data, jsize size) {
	jbyteArray ret =  env->NewByteArray(size);
	env->SetByteArrayRegion(ret, 0, size, (jbyte*)data);
	return ret;
}

JNIEXPORT jobject JNICALL Java_org_berkelium_java_impl_BufferImpl_createByteBuffer
  (JNIEnv* env, jclass, jlong addr, jsize size)
{
	return env->NewDirectByteBuffer((void*)addr, size);
}
