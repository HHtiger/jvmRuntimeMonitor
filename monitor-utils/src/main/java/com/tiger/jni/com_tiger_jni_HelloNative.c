#include <stdio.h>
#include "com_tiger_jni_HelloNative.h"

JNIEXPORT void JNICALL Java_com_tiger_jni_HelloNative_greeting(JNIEnv *env, jobject c1) {
    printf("Hello Native!!\n");
}