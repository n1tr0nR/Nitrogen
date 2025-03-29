package com.nitron.nitrogen.util;

public interface ScreenShaker {
    default boolean isScreenShaking(){
        return getScreenShakeIntensity() > 0;
    }
    int getScreenShakeDuration();
    float getScreenShakeIntensity();
    void setScreenShakeDuration(int duration);
    void setScreenShakeIntensity(float intensity);
    default void addScreenShake(float intensity, int duration){
        setScreenShakeIntensity(intensity);
        setScreenShakeDuration(duration);
    }
}
