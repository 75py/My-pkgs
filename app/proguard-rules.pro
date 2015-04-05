-dontskipnonpubliclibraryclasses
-optimizationpasses 3
-flattenpackagehierarchy
-keepattributes SourceFile,LineNumberTable
-printmapping map.txt

-keep public class com.google.android.gms.ads.** {
   public *;
}

-keep public class com.google.ads.** {
   public *;
}

-assumenosideeffects public class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
    public static *** wtf(...);
}
-assumenosideeffects class com.nagopy.android.mypkgs.util.DebugUtil {
    <methods>;
}
