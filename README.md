MovingImageView
===============
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MovingImageView-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1850)

Create a custom ImageView for moving image around the screen.

![Demo Screenshot 1][1]
![Demo Screenshot 2][2]

Usage
-----

To use MovingImageView, add the module into your project and start to build xml or java.

### XML
```xml
    <net.grobas.view.MovingImageView
        android:layout_width="match_parent"
        android:layout_height="250dp"
        android:src="@drawable/image"
        app:miv_load_on_create="true"
        app:miv_max_relative_size="3"
        app:miv_min_relative_offset="0.2"
        app:miv_start_delay="1000"
        app:miv_repetitions="-1"
        app:miv_speed="100" />
```

##### Properties:

* `app:miv_load_on_create` (boolean)    -> default true
* `app:miv_max_relative_size`  (float)  -> default 3.0f
* `app:miv_min_relative_offset` (float) -> default 0.2f
* `app:miv_start_delay` (integer)       -> default 0
* `app:miv_repetitions` (integer)       -> default -1
* `app:miv_speed` (integer)             -> default 50


### JAVA

```java
    MovingImageView image = (MovingImageView) findViewById(R.id.image);
    image.getMovingAnimator().setInterpolator(new BounceInterpolator());
    image.getMovingAnimator().setSpeed(100);
    image.getMovingAnimator().addCustomMovement().
            addDiagonalMoveToDownRight().
            addHorizontalMoveToLeft().
            addDiagonalMoveToUpRight().
            addVerticalMoveToDown().
            addHorizontalMoveToLeft().
            addVerticalMoveToUp().
            start();
```

License
-------

    Copyright 2014 Albert Grobas

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



[1]: ./art/sample01.gif
[2]: ./art/sample02.gif

