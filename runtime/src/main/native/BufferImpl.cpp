#include <org_berkelium_BufferImpl.h>

/*
 * Class:     org_berkelium_Buffer
 * Method:    createIntArray
 * Signature: (JJ)[I
 */
JNIEXPORT jintArray JNICALL Java_org_berkelium_BufferImpl_createIntArray
  (JNIEnv* env, jclass, jlong data, jsize size){
	size /= sizeof(jint);
	jintArray ret =  env->NewIntArray(size);
	env->SetIntArrayRegion(ret, 0, size, (jint*)data);
	return ret;
}

/*
 * Class:     org_berkelium_BufferImpl
 * Method:    createByteArray
 * Signature: (JJ)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_berkelium_BufferImpl_createByteArray
  (JNIEnv* env, jclass, jlong data, jsize size) {
	jbyteArray ret =  env->NewByteArray(size);
	env->SetByteArrayRegion(ret, 0, size, (jbyte*)data);
	return ret;
}


/*
 * Class:     org_berkelium_BufferImpl
 * Method:    createByteBuffer
 * Signature: (JI)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_berkelium_BufferImpl_createByteBuffer
  (JNIEnv* env, jclass, jlong addr, jsize size)
{
	return env->NewDirectByteBuffer((void*)addr, size);
}
