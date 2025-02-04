from snownlp import SnowNLP

def extract_keywords_snownlp(text, topK=5):
    # 创建SnowNLP对象
    s = SnowNLP(text)
    
    # 提取关键词
    keywords = s.keywords(limit=topK)
    return keywords

# 示例文本
text = u'''大家好，我是小明，来自计算机科学专业。

平时我一般在晚上9点左右睡觉，早上7点起床，而且我有午睡的习惯，这样可以让我在下午保持精力充沛。周末的时候，我喜欢做一些户外活动，比如运动和旅行，同时我也享受在家里安静地阅读。

在兴趣爱好方面，我特别喜欢听古典音乐，音乐可以让我放松心情。此外，我还喜欢看电影和阅读科幻小说，这些都让我觉得非常有趣。我自己也会写一些小故事和文章，这是我表达自己想法的一种方式。

关于学习，我更喜欢独立学习，这样可以让我按照自己的节奏来掌握知识。我未来的职业规划是成为一名工程师或者研究人员，因为我对技术和创新非常感兴趣。

希望能找到和我作息、兴趣爱好相似的舍友，我们可以一起分享生活的点滴，共同进步。'''

# 提取关键词
keywords = extract_keywords_snownlp(text, 10)

# 打印关键词
print("Keywords:", keywords)
