package org.protogalaxy.fractalfathom;

public @interface FractalFathomFeature {
    String name();
    String description() default "";
    FeatureType type() default FeatureType.FUNCTIONAL;
}
