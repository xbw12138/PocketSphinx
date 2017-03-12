# PocketSphinx

说明
首先去这个网站提交一些需要语音识别的文字，例如

```
开灯
关灯
```
提交一个txt文件到http://www.speech.cs.cmu.edu/tools/lmtool-new.html
然后从该网站下载下来生成的dic，lm文件
然后dic文件添加拼音

```
关灯	g uan d eng
开灯	k ai d eng
```
这些拼音需要在字典里查找

然后替换到assets即可



