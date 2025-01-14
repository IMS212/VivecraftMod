package com.example.vivecraftfabric;

import net.coderbot.iris.Iris;
import net.irisshaders.iris.api.v0.IrisApi;

import java.io.IOException;

public class IrisHelper {

    public static int ShaderLight() {
        if (IrisApi.getInstance().isShaderPackInUse()) {
            return 8;
        }
        return 4;
    }

    public static void setShadersActive(boolean bl) {
        IrisApi.getInstance().getConfig().setShadersEnabledAndApply(bl);
    }

    public static void reload() {
        try {
            Iris.reload();
        } catch (IOException e) {
            System.err.println("Error reloading shaders on Frame Buffer reinit");
            e.printStackTrace();
        }

    }
}
