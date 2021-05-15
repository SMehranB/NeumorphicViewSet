
# Neumorphic View Set

## A set of views with neumorphic design
 
## Features!

 •	 Button
 
 •	 Image Button
 
 •	 Check box
 
 •	 Seek Bar
 
 •   Radio Button
 
 •	 Switch

## Screen recording

 <img src="./screen_recording.gif" height="600"> <img src="./screen_recording.gif" height="600">
 
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
  android:id="@+id/btnNeumorphic"  
  android:layout_width="wrap_content"  
  android:layout_height="wrap_content"  
  android:layout_gravity="center_horizontal"  
  app:nb_JutSize="large"  
  app:nb_backgroundColor="@color/neuSecondaryColor"  
  app:nb_cornerRadius="20dp"  
  app:nb_drawableEnd="@drawable/outline_shopping_cart_24"  
  app:nb_drawablePadding="16dp"  
  app:nb_drawableStart="@drawable/baseline_face_24"  
  app:nb_drawableTint="@color/white"  
  app:nb_enabled="false"  
  app:nb_lightDensity="0.4"  
  app:nb_shadowDensity="0.5"  
  app:nb_text="Neumorphic Button"  
  app:nb_textColor="@color/white"  
  app:nb_textSize="24dp"  
  app:nb_textStyle="bold" />
 ```

### Image Button
```xml
<com.smb.neumorphicviewset.NeuImageButton  
  android:id="@+id/btnPlay"  
  android:layout_width="wrap_content"  
  android:layout_height="wrap_content"  
  android:layout_gravity="center_horizontal"  
  app:nib_JutSize="normal"  
  app:nib_backgroundColor="@color/neuSecondaryColor"  
  app:nib_cornerRadius="100dp"  
  app:nib_drawableDimension="50dp"  
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
  android:layout_gravity="center_horizontal"  
  app:ncb_JutSize="large"  
  app:ncb_backgroundColor="@color/neuSecondaryColor"  
  app:ncb_text="Check Me!"  
  app:ncb_textColor="@color/white"  
  app:ncb_textStyle="bold" />
```

### Switch
```xml
<com.smb.neumorphicviewset.NeuSwitch  
  android:id="@+id/neuSwitch"  
  android:layout_width="wrap_content"  
  android:layout_height="wrap_content"  
  android:layout_gravity="center_horizontal"  
  app:ns_backgroundColor="@color/neuSecondaryColor"  
  app:ns_checked="true"  
  app:ns_handleColor="#11FF00"  
  app:ns_shadowDensity="0.6" />
```

### Seek Bar
```xml
<com.smb.neumorphicviewset.NeuSeekBar  
  android:id="@+id/neuSeekBar"  
  android:layout_width="match_parent"  
  android:layout_height="wrap_content"  
  app:nsb_JutSize="normal"  
  app:nsb_ProgressBarHeight="16dp"  
  app:nsb_backgroundColor="@color/neuSecondaryColor"  
  app:nsb_enabled="false"  
  app:nsb_handleRadius="12dp"  
  app:nsb_lightDensity="0.5"  
  app:nsb_shadowDensity="0.7" />
```

### Radio Button & Radio Group
```xml
<com.smb.neumorphicviewset.NeuRadioGroup  
  android:id="@+id/neuRadioGroup"  
  android:layout_width="match_parent"  
  android:orientation="vertical"  
  android:background="@color/neuPrimaryColor"  
  android:layout_height="wrap_content">  
  
	 <com.smb.neumorphicviewset.NeuRadioButton
	  android:id="@+id/rb1"  
	  app:nrb_text="Radio button 1"  
	  app:nrb_textColor="@color/white"  
	  android:layout_width="wrap_content"  
	  android:layout_height="wrap_content" />  
	  
	 <com.smb.neumorphicviewset.NeuRadioButton 
	  android:id="@+id/rb2"  
	  app:nrb_text="Radio Button 2"  
	  app:nrb_textColor="@color/white"  
	  android:layout_width="wrap_content"  
	  android:layout_height="wrap_content" />  
	  
	 <com.smb.neumorphicviewset.NeuRadioButton 
	  android:id="@+id/rb3"  
	  app:nrb_text="Radio Button 3"  
	  app:nrb_textColor="@color/white"  
	  android:layout_width="wrap_content"  
	  android:layout_height="wrap_content" />  
	  
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
