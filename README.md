# AmeEnjin

<blockquote>
Copyright 2019 Arkadiusz Kostyra
<br>
<br>
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</blockquote>
<h2>About</h2>

LWJGL 2-based library to create typing games with chars drop.
<br>

Chars dropping and typing is realized as fight with opponent. Chars drop series are defined as npc abilities, with defined size, power, speed and cost. To use abilities are required AP (action points), obtained on turn begin.

Games may have plot, realized by simple dialogues and static text screens with or without background.

Is possible to add  credits rolling from bottom to top,  contains text and images.
<br>
<br>
<strong>By default, library uses fonts:</strong><br>
<a href = "https://mozilla.github.io/Fira/" target = "_blank">Fira Sans</a><br>
<a href = "http://font.gloomy.jp/honoka-antique-kaku-dl.html" target = "_blank">Honoka Antique Kaku (ほのかアンティーク角)</a><br>
<br>
Fonts are included in zip archive.
<br>
<br>
Default kana images are included in zip archive.
<br>
<br>
<br>
<a href = "https://www.dropbox.com/s/383odzayit5lw91/AmeEnjinProject.zip?dl=0" target = "_blank"><h3>Download</h3></a>
<br>
<br>
To create game, extend engine.Project class and add to main method function:

<code>
engine.Application.run(new YourClassExtendingProjectClass());
</code>
<br>
<a href = "https://www.dropbox.com/s/ptxe1eahzis0fii/AmeEnjin_Project_Javadoc.zip?dl=0" target = "_blank">Download Project class Javadoc</a>
<br>
Notice: Game created with version 0.2 may be displayed only in window 768x1024px
<br>
<br>
<strong>Required libraries:</strong>
<br>
<a href ="http://legacy.lwjgl.org" target="_blank">LWJGL 2</a><br>
<a href ="http://slick.ninjacave.com/slick-util/" target="_blank">Slick-Util</a><br>
<a href ="https://github.com/AK-21/uJavniacz" target="_blank">uJavniacz</a><br>

