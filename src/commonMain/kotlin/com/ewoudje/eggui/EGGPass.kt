package com.ewoudje.eggui

interface EGGPass {
    data class Render(val renderer: EGGRenderer) : EGGPass, EGGRenderer by renderer
}