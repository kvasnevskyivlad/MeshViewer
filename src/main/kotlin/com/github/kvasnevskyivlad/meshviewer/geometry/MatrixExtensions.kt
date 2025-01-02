package com.github.kvasnevskyivlad.meshviewer.geometry

import javax.vecmath.Matrix4f

fun Matrix4f.toColumnMajor(): Matrix4f = Matrix4f(
    m00, m10, m20, m30,
    m01, m11, m21, m31,
    m02, m12, m22, m32,
    m03, m13, m23, m33
)

fun Matrix4f.toFloatArray(): FloatArray = floatArrayOf(
    m00, m01, m02, m03,
    m10, m11, m12, m13,
    m20, m21, m22, m23,
    m30, m31, m32, m33
)