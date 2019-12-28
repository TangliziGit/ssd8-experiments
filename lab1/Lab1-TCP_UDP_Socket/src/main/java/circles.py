from PIL import Image, ImageDraw

# 创建一个200x200的透明度为100的画布
img = Image.new('RGB', (794, 1123), (255, 255, 255))
draw = ImageDraw.Draw(img)

# 画一个直径为100的正圆，内部填充为白色，轮廓为红色，轮廓（为内部轮廓）宽度为10。
R=140//2
interval=38
draw.ellipse((20, 20, 20+R*2, 20+R*2), outline='black', width=2)

img.save("circle.png")
