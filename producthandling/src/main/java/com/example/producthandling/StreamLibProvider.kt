package com.example.producthandling

import com.example.producthandling.internal.StreamLibImpl


object StreamLibProvider {
    val instance: StreamLibAPI by lazy { StreamLibImpl() }
}
