package com.ewoudje.eggui

interface EGGPass<P: EGGPlatform<P>> {
    data class Render<P: EGGPlatform<P>>(val renderer: EGGRenderer<P>) : EGGPass<P>, EGGRenderer<P> by renderer
}