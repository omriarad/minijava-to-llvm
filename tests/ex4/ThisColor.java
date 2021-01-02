class Main {
	public static void main(String[] args) {
		System.out.println((new Test()).test());
	}
}

class Color {
	int[] rgb;

	Color colorField;

	public boolean initColorArray(int[] newRGB) {
		rgb = new int[3];
		rgb[0] = (newRGB)[0];
		rgb[1] = (newRGB)[1];
		rgb[2] = (newRGB)[2];
		return true;
	}

	public boolean initColorInt(int r, int g, int b) {
		rgb = new int[3];
		rgb[0] = r;
		rgb[1] = g;
		rgb[2] = b;
		return true;
	}

	public int[] getRGB() {
		return rgb;
	}

	public Color addIn(Color otherColor) {
		int[] otherRGB;
		otherRGB = (otherColor).getRGB();
		rgb[0] = ((rgb)[0]) + ((otherRGB)[0]);
		rgb[1] = ((rgb)[1]) + ((otherRGB)[1]);
		rgb[2] = ((rgb)[2]) + ((otherRGB)[2]);
		return this;
	}

	public Color addOut(Color otherColor) {
		Color result;
		result = (otherColor).addIn(this);
		return otherColor;
	}

	public Color switchTo(Color otherColor) {
		otherColor = this;
		return otherColor;
	}

	public Color doubleColor() {
		Color doubled;
		int[] doubledRGB;
		doubled = this;
		doubledRGB = (doubled).getRGB();
		doubledRGB[0] = ((doubledRGB)[0]) * (2);
		doubledRGB[1] = ((doubledRGB)[1]) * (2);
		doubledRGB[2] = ((doubledRGB)[2]) * (2);
		return doubled;
	}

	public Color setColorField() {
		colorField = this;
		return this;
	}

	public Color getColorField() {
		return colorField;
	}

	public int getSum() {
		return ((rgb)[0]) + (((rgb)[1]) + ((rgb)[2]));
	}

}

class Test {
	Color c1;

	Color c2;

	public boolean initTest() {
		int[] c1RGB;
		int c2R;
		int c2G;
		int c2B;
		boolean placeholder;
		c1 = new Color();
		c2 = new Color();
		c1RGB = new int[3];
		c1RGB[0] = 10;
		c1RGB[1] = 10;
		c1RGB[2] = 10;
		placeholder = (c1).initColorArray(c1RGB);
		c2R = 20;
		c2G = 20;
		c2B = 20;
		placeholder = (c2).initColorInt(c2R, c2G, c2B);
		return true;
	}

	public int test() {
		boolean placeholder;
		Color result;
		placeholder = (this).initTest();
		result = (c1).addIn(c2);
		System.out.println((result).getSum());
		result = (c1).addOut(c2);
		System.out.println((result).getSum());
		result = (c2).switchTo(c1);
		System.out.println((c1).getSum());
		c1 = (c1).doubleColor();
		System.out.println((c1).getSum());
		result = (c2).doubleColor();
		System.out.println((c2).getSum());
		result = (c1).setColorField();
		result = (c1).getColorField();
		System.out.println((result).getSum());
		return 0;
	}

}

