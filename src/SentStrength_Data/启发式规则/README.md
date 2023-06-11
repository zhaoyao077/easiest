# 启发式规则

### 1. 对字母“X”的误解：

- 在非正式的计算机中介聊天中，字母“X”(不区分大小写)通常用于表示“Kiss”的动作，这是一种积极的情绪，因此记录在SentiStrength的字典中。但是，在技术领域，该字母通常用作通配符。例如，以下注释中的序列“1.4.x”用于指示版本/发行版的集合。
  “Integrated in Apache Wicket 1.4.x ...”
  由于SentiStrength使用点（.）作为分隔符将文本拆分为句子，因此“x”被认为是一个单词的句子，并被误解为表达了积极的情绪。

- 解决方案：考虑到是软工文本，可以直接修改词典，将单个字母“x”的情绪值删去。

### 2. 字典中缺少感伤词：

- 由于SentiStrength的词汇方法在很大程度上依赖于其单词列表词典，因此当词典中没有文本中使用的感性词时，该工具通常无法检测到某些文本中的情感。
  例如，以下两条评论中的“Apology”和“Oops”一词表达了负面情绪，但SentiStrength无法检测到它们，因为这些词未包含在其字典中。
  “...This is indeed not an issue. My apologies ...”
  “Oops ; issue comment had wrong ticket number in it ...”

- 解决方案：用新的感性词语和否定语进行扩展，将非正式的情感词“Woops”，“Oops”等以及缺少的正式的单词“Apology”添加到词典中，作为具有适当情感极性的情感术语。修改后的规则词典为/EmotionLookupTable_Software.txt。

### 3. 使用中和剂(neutralizers)

- 从数据的探索性研究中观察到，如果一个词前面有任何中和词，如“Would”“Could”“Should”和“Might”，则该词的情绪可以被中和。
  例如，在句子“It would be good if the test could be completed soon”中，被前一个词“would”中和后的积极情绪词“good”实际上并没有表达任何的情绪。

- 方案：在sentistrength/Sentence.java中添加了一个方法，使其能够正确检测句子中此类中和词的用法，从而在情感检测中更准确。



内容参考论文《SentiStrength-SE: Exploiting domain specificity for improved sentiment analysis in software engineering text》

文档链接：[SentiStrength-SE: Exploiting domain specificity for improved sentiment analysis in software engineering text - ScienceDirect](https://www.sciencedirect.com/science/article/pii/S0164121218301675?via%3Dihub)
