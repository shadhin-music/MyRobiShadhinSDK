# ShadhinSDK
[![](https://jitpack.io/v/GakkMedia/MYBLShadhinSDK-android.svg)](https://jitpack.io/#GakkMedia/MYBLShadhinSDK-android)

## ScreenShot
<p align="center">
  <a style="text-decoration:none" area-label="Android">
<img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/1.jpg" width="100" height="200" />
  </a>
  <a style="text-decoration:none" area-label="Build Status">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/2.jpg" width="100" height="200" />
  </a>
  <a style="text-decoration:none" area-label="Min API: 21">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/3.jpg" width="100" height="200" />
  </a>
  <a style="text-decoration:none" area-label="Play Store">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/4.jpg" width="100" height="200" />
  </a>
  <a style="text-decoration:none" area-label="License: GPL v3">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/5.jpg" width="100" height="200" />
  </a>
  <a style="text-decoration:none" area-label="Twitter Follow">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/6.jpg" width="100" height="200" />
  </a>
  <a style="text-decoration:none" area-label="Crowdin">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/7.jpg" width="100" height="200" />
  </a>
    <a style="text-decoration:none" area-label="Crowdin">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/8.jpg" width="100" height="200" />
  </a>
    <a style="text-decoration:none" area-label="Crowdin">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/9.jpg" width="100" height="200" />
  </a>
    <a style="text-decoration:none" area-label="Crowdin">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/10.jpg" width="100" height="200" />
  </a>
    <a style="text-decoration:none" area-label="Crowdin">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/11.jpg" width="100" height="200" />
  </a>
    <a style="text-decoration:none" area-label="Crowdin">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/12.jpg" width="100" height="200" />
  </a>
    <a style="text-decoration:none" area-label="Crowdin">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/13.jpg" width="100" height="200" />
  </a>
    <a style="text-decoration:none" area-label="Crowdin">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/14.jpg" width="100" height="200" />
  </a>
    <a style="text-decoration:none" area-label="Crowdin">
    <img src="https://raw.githubusercontent.com/GakkMedia/ShadhinSDK/dev_rezaul/sampleImage/15.jpg" width="100" height="200" />
  </a>
</p>

    
## Setup
**Root level**
      
    allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
		}
    }

**Gradle**

    dependencies {
       implementation 'com.github.shadhin-music:MYBLMusicSDK:Tag'
    }
    
 ## Usage

More usages about ShadhinSDK, please see the sample.

At first initialize SDK. And implement
     
     interface ShadhinSDKCallback {
     //after initializeSDK this method will be notify
     //about the token status
     fun tokenStatus(isTokenValid: Boolean, error: String)
     }

     ShadhinMusicSdkCore.initializeSDK(token: String, refSdkCall: ShadhinSDKCallback)

Open MusicFragment and Music Activity

     ShadhinMusicSdkCore.getMusicFragment()

     ShadhinMusicSdkCore.openMusic(reqContext: Context)
     
Open API Activity

     ShadhinMusicSdkCore.openPatch(reqContext: Context, requestId: Int)

API Activity Request Type
     
     get rcode from API
     
Call destroySDK method when Root Activity will destroy
     
     override fun onDestroy() {
        ShadhinMusicSdkCore.destroySDK(applicationContext)
        super.onDestroy()
    }
  
## Author
Rezaul Khan, rezaulkhan.gakk@gmail.com

[More about developer complany: Shadhin Music Limited <img src="https://shadhinmusic.com/img/shadhinlogo.svg" width="200" height="40" />](https://shadhinmusic.com)

## License

    MIT License

    Copyright (c) 2022 Shadhin Music Limited

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
