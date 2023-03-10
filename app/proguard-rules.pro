# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class in.opening.area.zustapp.home.models.** { *; }
-keep class in.opening.area.zustapp.login.model.** { *; }
-keep class in.opening.area.zustapp.uiModels.** { *; }
-keep class in.opening.area.zustapp.profile.models.** { *; }
-keep class in.opening.area.zustapp.product.model.** { *; }
-keep class in.opening.area.zustapp.payment.models.** { *; }
-keep class in.opening.area.zustapp.orderSummary.model.** { *; }
-keep class in.opening.area.zustapp.orderHistory.models.** { *; }
-keep class in.opening.area.zustapp.orderDetail.models.** { *; }
-keep class in.opening.area.zustapp.network.requestBody.** { *; }
-keep class in.opening.area.zustapp.locationManager.models.** { *; }
-keep class in.opening.area.zustapp.helper.model.** { *; }
-keep class in.opening.area.zustapp.fcm.FcmReqBodyModel{*;}
-keep class in.opening.area.zustapp.fcm.OrderDetailDeepLinkModel{*;}
-keep class in.opening.area.zustapp.data.** { *; }
-keep class in.opening.area.zustapp.coupon.model.** { *; }
-keep class in.opening.area.zustapp.address.model.** { *; }
-keep class in.opening.area.zustapp.utility.UserCustomError{ *; }
-keep class in.opening.area.zustapp.utility.PagingMetadata{ *; }














