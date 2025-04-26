package com.nitron.nitrogen.util;

import com.nitron.nitrogen.Nitrogen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

public class NotASupporerException extends ExceptionInInitializerError{
    public static void notASupporter(ClientPlayerEntity player){
        if(player != null){
            if (!SupporterUtils.isPlayerSupporter(player) && NitrogenAPI.IS_SUPPORTER_REQUIRED){
                throw new CrashException(new CrashReport("You are not a supporter!", new NotASupporerException()));
            }
        }
    }
}
