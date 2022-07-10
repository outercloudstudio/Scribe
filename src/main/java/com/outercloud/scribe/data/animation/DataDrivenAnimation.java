package com.outercloud.scribe.data.animation;

import com.google.gson.JsonElement;
import com.outercloud.scribe.Scribe;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

import java.util.Iterator;
import java.util.Map;

public class DataDrivenAnimation {
    public static Animation getAnimation(Identifier identifier){
        DataDrivenAnimationData data = Scribe.getDataDrivenAnimation(identifier);

        if(!Scribe.initializedDataDrivenFeatures) Scribe.LOGGER.error("Fetched data driven animation without initializing data driven features!");

        if(data == null) Scribe.LOGGER.error("Data driven animation " + identifier + " does not exist!");

        Animation.Builder builder = Animation.Builder.create(data.GetLength());

        Iterator<Map.Entry<String, JsonElement>> boneIterator = data.GetBones();

        while (boneIterator.hasNext()){
            Map.Entry<String, JsonElement> boneEntry = boneIterator.next();

            Iterator<Map.Entry<String, JsonElement>> operationIterator = data.GetOperations(boneEntry.getKey());

            while (operationIterator.hasNext()){
                Map.Entry<String, JsonElement> operationEntry = operationIterator.next();

                Iterator<Map.Entry<String, JsonElement>> keyframeIterator = data.GetKeyframes(boneEntry.getKey(), operationEntry.getKey());

                Keyframe[] keyframes = new Keyframe[data.GetKeyframeCount(boneEntry.getKey(), operationEntry.getKey())];

                int i = 0;

                while (keyframeIterator.hasNext()){
                    Map.Entry<String, JsonElement> keyframeEntry = keyframeIterator.next();

                    DataDrivenAnimationData.InterpolationType interpolationType = data.GetInterpolationType(boneEntry.getKey(), operationEntry.getKey(), keyframeEntry.getKey());

                    float x = data.GetKeyframeCoord(boneEntry.getKey(), operationEntry.getKey(), keyframeEntry.getKey(), "x");
                    float y = data.GetKeyframeCoord(boneEntry.getKey(), operationEntry.getKey(), keyframeEntry.getKey(), "y");
                    float z = data.GetKeyframeCoord(boneEntry.getKey(), operationEntry.getKey(), keyframeEntry.getKey(), "z");

                    Vec3f vec = new Vec3f(x, y, z);

                    if(operationEntry.getKey().equals("translate")){
                        vec = AnimationHelper.method_41823(x, y, z);
                    } else if(operationEntry.getKey().equals("rotate")){
                        vec = AnimationHelper.method_41829(x, y, z);
                    } else if(operationEntry.getKey().equals("scale")){
                        vec = AnimationHelper.method_41822(x, y, z);
                    } else{
                        Scribe.LOGGER.warn("Unknown operation " + operationEntry.getKey() + " of bone " + boneEntry.getKey() + " in animation " + identifier.toString());
                    }

                    if(interpolationType == DataDrivenAnimationData.InterpolationType.LINEAR){
                        keyframes[i] = new Keyframe(Float.parseFloat(keyframeEntry.getKey()), vec, Transformation.Interpolations.field_37884);
                    }else {
                        keyframes[i] = new Keyframe(Float.parseFloat(keyframeEntry.getKey()), vec, Transformation.Interpolations.field_37885);
                    }

                    i++;
                }

                if(operationEntry.getKey().equals("translate")){
                    builder.addBoneAnimation(boneEntry.getKey(), new Transformation(Transformation.Targets.TRANSLATE, keyframes));
                } else if(operationEntry.getKey().equals("rotate")){
                    builder.addBoneAnimation(boneEntry.getKey(), new Transformation(Transformation.Targets.ROTATE, keyframes));
                } else if(operationEntry.getKey().equals("scale")){
                    builder.addBoneAnimation(boneEntry.getKey(), new Transformation(Transformation.Targets.SCALE, keyframes));
                } else{
                    Scribe.LOGGER.warn("Unknown operation " + operationEntry.getKey() + " of bone " + boneEntry.getKey() + " in animation " + identifier.toString());
                }
            }
        }

        if(data.GetLooping()) builder.looping();

        return builder.build();
    }
}
