package com.example.sergey.myapplication

data class User(val id: Int, val content: String, val details: String) {
    override fun toString(): String = content + id
}