
# Neumorphic View Set [![](https://jitpack.io/v/SMehranB/NeumorphicViewSet.svg)](https://jitpack.io/#SMehranB/NeumorphicViewSet)

## A set of views with neumorphic design
 
## Features!

 •	 Button
 
 •	 Image Button
 
 •	 Check box
 
 •	 Seek Bar
 
 •   Radio Button
 
 •	 Switch

## Screen recording

 <img src="./screenshot.png" height="600">
 
# Install
 
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
## Gradle

```groovy
dependencies {
	 implementation 'com.github.SMehranB:NeumorphicViewSet:1.0.0'
}
```
## Maven
```xml
<dependency>
	<groupId>com.github.SMehranB</groupId>
	<artifactId>NeumorphicViewSet</artifactId>
	<version>1.0.0</version>
</dependency>
 ```
# Use
 
## XML

### Button
```xml
<com.smb.neumorphicviewset.NeuButton
    android:id="@+id/btnFour"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal"
    app:nb_ButtonColor="@color/neuSecondaryColor"
    app:nb_HorizontalPadding="32dp"
    app:nb_JutSize="normal"
    app:nb_cornerRadius="20dp"
    app:nb_drawableEnd="@drawable/baseline_face_24"
    app:nb_drawablePadding="24dp"
    app:nb_drawableStart="@drawable/baseline_face_24"
    app:nb_drawableTint="@color/white"
    app:nb_lightDensity="0.4"
    app:nb_shadowDensity="0.5"
    app:nb_text="Create Profile"
    app:nb_textColor="@color/white"
    app:nb_textSize="16dp"
    app:nb_textStyle="bold" />
 ```

### Image Button
```xml
<com.smb.neumorphicviewset.NeuImageButton
    android:id="@+id/btnPlay"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:nib_ButtonColor="@color/neuSecondaryColor"
    app:nib_HorizontalPadding="16dp"
    app:nib_JutSize="large"
    app:nib_VerticalPadding="16dp"
    app:nib_cornerRadius="100dp"
    app:nib_drawableDimension="60dp"
    app:nib_drawableTint="@color/white"
    app:nib_lightDensity="0.4"
    app:nib_shadowDensity="0.5" />
```

### Check Box
```xml
<com.smb.neumorphicviewset.NeuCheckBox
    android:id="@+id/neu_check_box"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="start"
    app:ncb_CheckBoxColor="@color/neuSecondaryColor_light"
    app:ncb_CheckMarkColor="#11FF00"
    app:ncb_JutSize="large"
    app:ncb_checked="true"
    app:ncb_lightDensity="0.8"
    app:ncb_shadowDensity="0.6"
    app:ncb_text="Save my info"
    app:ncb_textColor="@color/neuTextColor" />
```

### Switch
```xml
<com.smb.neumorphicviewset.NeuSwitch
    android:id="@+id/neuSwitch"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="start"
    app:ns_JutSize="large"
    app:ns_SwitchColor="@color/neuSecondaryColor_light"
    app:ns_checked="true"
    app:ns_handleColor="#11FF00"
    app:ns_lightDensity="0.8"
    app:ns_shadowDensity="0.6"
    app:ns_text="Insure My Package"
    app:ns_textColor="@color/neuTextColor" />
```

### Seek Bar
```xml
<com.smb.neumorphicviewset.NeuSeekBar
    android:id="@+id/neuSeekBar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_weight="2"
    app:nsb_JutSize="normal"
    app:nsb_Progress="30"
    app:nsb_ProgressBarHeight="16dp"
    app:nsb_ProgressColor="@color/neuHandleColor"
    app:nsb_SeekBarColor="@color/neuSecondaryColor"
    app:nsb_handleRadius="12dp"
    app:nsb_lightDensity="0.5"
    app:nsb_shadowDensity="0.6" />
```

### Radio Button & Radio Group
```xml
<com.smb.neumorphicviewset.NeuRadioGroup
    android:id="@+id/neuRadioGroup"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:orientation="horizontal">

    <com.smb.neumorphicviewset.NeuRadioButton
        android:id="@+id/rb1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:nrb_CheckedColor="#11FF00"
        app:nrb_JutSize="large"
        app:nrb_RadioButtonColor="@color/neuSecondaryColor_light"
        app:nrb_lightDensity="0.8"
        app:nrb_text="Normal Delivery"
        app:nrb_textColor="@color/neuTextColor" />

    <com.smb.neumorphicviewset.NeuRadioButton
        android:id="@+id/rb2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:nrb_CheckedColor="#11FF00"
        app:nrb_JutSize="large"
        app:nrb_checked="true"
        app:nrb_RadioButtonColor="@color/neuSecondaryColor_light"
        app:nrb_lightDensity="0.8"
        app:nrb_text="Express Delivery"
        app:nrb_textColor="@color/neuTextColor" />

</com.smb.neumorphicviewset.NeuRadioGroup>
```

## Kotlin
### Button 
```kotlin
val neuButton = NeuButton(this)  
val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)  
  
neuButton.apply {  
  layoutParams = params  
    setText("Mehran", 24, Color.CYAN)  
    setBackgroundParams(Color.RED, 50)  
    setDrawableParams(R.drawable.baseline_face_24, R.drawable.outline_shopping_cart_24, null, 16)  
    setJutParams(Jut.LARGE)  
    setDrawableDimension(30)  
    setTextPaddings(16, 16)  
    setTypeface(Typeface.BOLD_ITALIC, R.font.alsscrp)  
    disabledTextColor = Color.BLUE  
}  
  
binding.viewHolder.addView(neuButton)
```

## 📄 License
```text
MIT License

Copyright (c) 2021 Seyed Mehran Behbahani

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
