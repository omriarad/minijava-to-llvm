@.Color_vtable = global [10 x i8*] [i8* bitcast (i1 (i8*, i32*)* @Color.initColorArray to i8*), i8* bitcast (i1 (i8*, i32, i32, i32)* @Color.initColorInt to i8*), i8* bitcast (i32* (i8*)* @Color.getRGB to i8*), i8* bitcast (i8* (i8*, i8*)* @Color.addIn to i8*), i8* bitcast (i8* (i8*, i8*)* @Color.addOut to i8*), i8* bitcast (i8* (i8*, i8*)* @Color.switchTo to i8*), i8* bitcast (i8* (i8*)* @Color.doubleColor to i8*), i8* bitcast (i8* (i8*)* @Color.setColorField to i8*), i8* bitcast (i8* (i8*)* @Color.getColorField to i8*), i8* bitcast (i32 (i8*)* @Color.getSum to i8*)]

@.Test_vtable = global [2 x i8*] [i8* bitcast (i1 (i8*)* @Test.initTest to i8*), i8* bitcast (i32 (i8*)* @Test.test to i8*)]

declare i8* @calloc(i32, i32)
declare i32 @printf(i8*, ...)
declare void @exit(i32)

@_cint = constant [4 x i8] c"%d\0a\00"
@_cOOB = constant [15 x i8] c"Out of bounds\0a\00"
define void @print_int(i32 %i) {
	%_str = bitcast [4 x i8]* @_cint to i8*
	call i32 (i8*, ...) @printf(i8* %_str, i32 %i)
	ret void
}

define void @throw_oob() {
	%_str = bitcast [15 x i8]* @_cOOB to i8*
	call i32 (i8*, ...) @printf(i8* %_str)
	call void @exit(i32 1)
	ret void
}


define i32 @main() {
	%_0 = call i8* @calloc(i32 1, i32 24)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [2 x i8*], [2 x i8*]* @.Test_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	%_3 = bitcast i8* %_0 to i8***
	%_4 = load i8**, i8*** %_3
	%_5 = getelementptr i8*, i8** %_4, i32 1
	%_6 = load i8*, i8** %_5
	%_7 = bitcast i8* %_6 to i32 (i8*)*
	%_8 = call i32 %_7(i8* %_0)
	call void (i32) @print_int(i32 %_8)
	ret i32 0
}

define i1 @Color.initColorArray(i8* %this, i32* %.newRGB) {
	%newRGB = alloca i32*
	store i32* %.newRGB, i32** %newRGB
	%_0 = icmp slt i32 3, 0
	br i1 %_0, label %arr_alloc0, label %arr_alloc1
arr_alloc0:
	call void @throw_oob()
	br label %arr_alloc1
arr_alloc1:
	%_1 = add i32 3, 1
	%_2 = call i8* @calloc(i32 4, i32 %_1)
	%_3 = bitcast i8* %_2 to i32*
	store i32 3, i32* %_3
	%_4 = getelementptr i8, i8* %this, i32 8
	%_5 = bitcast i8* %_4 to i32**
	store i32* %_3, i32** %_5
	%_6 = getelementptr i8, i8* %this, i32 8
	%_7 = bitcast i8* %_6 to i32**
	%_8 = load i32*, i32** %_7
	%_9 = load i32*, i32** %newRGB
	%_10 = icmp slt i32 0, 0
	br i1 %_10, label %arr_alloc2, label %arr_alloc3
arr_alloc2:
	call void @throw_oob()
	br label %arr_alloc3
arr_alloc3:
	%_11 = getelementptr i32, i32* %_9, i32 0
	%_12 = load i32, i32* %_11
	%_13 = icmp sle i32 %_12, 0
	br i1 %_13, label %arr_alloc4, label %arr_alloc5
arr_alloc4:
	call void @throw_oob()
	br label %arr_alloc5
arr_alloc5:
	%_14 = add i32 0, 1
	%_15 = getelementptr i32, i32* %_9, i32 %_14
	%_16 = load i32, i32* %_15
	%_17 = icmp slt i32 0, 0
	br i1 %_17, label %arr_alloc6, label %arr_alloc7
arr_alloc6:
	call void @throw_oob()
	br label %arr_alloc7
arr_alloc7:
	%_18 = getelementptr i32, i32* %_8, i32 0
	%_19 = load i32, i32* %_18
	%_20 = icmp sle i32 %_19, 0
	br i1 %_20, label %arr_alloc8, label %arr_alloc9
arr_alloc8:
	call void @throw_oob()
	br label %arr_alloc9
arr_alloc9:
	%_21 = add i32 0, 1
	%_22 = getelementptr i32, i32* %_8, i32 %_21
	store i32 %_16, i32* %_22
	%_23 = getelementptr i8, i8* %this, i32 8
	%_24 = bitcast i8* %_23 to i32**
	%_25 = load i32*, i32** %_24
	%_26 = load i32*, i32** %newRGB
	%_27 = icmp slt i32 1, 0
	br i1 %_27, label %arr_alloc10, label %arr_alloc11
arr_alloc10:
	call void @throw_oob()
	br label %arr_alloc11
arr_alloc11:
	%_28 = getelementptr i32, i32* %_26, i32 0
	%_29 = load i32, i32* %_28
	%_30 = icmp sle i32 %_29, 1
	br i1 %_30, label %arr_alloc12, label %arr_alloc13
arr_alloc12:
	call void @throw_oob()
	br label %arr_alloc13
arr_alloc13:
	%_31 = add i32 1, 1
	%_32 = getelementptr i32, i32* %_26, i32 %_31
	%_33 = load i32, i32* %_32
	%_34 = icmp slt i32 1, 0
	br i1 %_34, label %arr_alloc14, label %arr_alloc15
arr_alloc14:
	call void @throw_oob()
	br label %arr_alloc15
arr_alloc15:
	%_35 = getelementptr i32, i32* %_25, i32 0
	%_36 = load i32, i32* %_35
	%_37 = icmp sle i32 %_36, 1
	br i1 %_37, label %arr_alloc16, label %arr_alloc17
arr_alloc16:
	call void @throw_oob()
	br label %arr_alloc17
arr_alloc17:
	%_38 = add i32 1, 1
	%_39 = getelementptr i32, i32* %_25, i32 %_38
	store i32 %_33, i32* %_39
	%_40 = getelementptr i8, i8* %this, i32 8
	%_41 = bitcast i8* %_40 to i32**
	%_42 = load i32*, i32** %_41
	%_43 = load i32*, i32** %newRGB
	%_44 = icmp slt i32 2, 0
	br i1 %_44, label %arr_alloc18, label %arr_alloc19
arr_alloc18:
	call void @throw_oob()
	br label %arr_alloc19
arr_alloc19:
	%_45 = getelementptr i32, i32* %_43, i32 0
	%_46 = load i32, i32* %_45
	%_47 = icmp sle i32 %_46, 2
	br i1 %_47, label %arr_alloc20, label %arr_alloc21
arr_alloc20:
	call void @throw_oob()
	br label %arr_alloc21
arr_alloc21:
	%_48 = add i32 2, 1
	%_49 = getelementptr i32, i32* %_43, i32 %_48
	%_50 = load i32, i32* %_49
	%_51 = icmp slt i32 2, 0
	br i1 %_51, label %arr_alloc22, label %arr_alloc23
arr_alloc22:
	call void @throw_oob()
	br label %arr_alloc23
arr_alloc23:
	%_52 = getelementptr i32, i32* %_42, i32 0
	%_53 = load i32, i32* %_52
	%_54 = icmp sle i32 %_53, 2
	br i1 %_54, label %arr_alloc24, label %arr_alloc25
arr_alloc24:
	call void @throw_oob()
	br label %arr_alloc25
arr_alloc25:
	%_55 = add i32 2, 1
	%_56 = getelementptr i32, i32* %_42, i32 %_55
	store i32 %_50, i32* %_56
	ret i1 1
}

define i1 @Color.initColorInt(i8* %this, i32 %.r, i32 %.g, i32 %.b) {
	%r = alloca i32
	store i32 %.r, i32* %r
	%g = alloca i32
	store i32 %.g, i32* %g
	%b = alloca i32
	store i32 %.b, i32* %b
	%_0 = icmp slt i32 3, 0
	br i1 %_0, label %arr_alloc0, label %arr_alloc1
arr_alloc0:
	call void @throw_oob()
	br label %arr_alloc1
arr_alloc1:
	%_1 = add i32 3, 1
	%_2 = call i8* @calloc(i32 4, i32 %_1)
	%_3 = bitcast i8* %_2 to i32*
	store i32 3, i32* %_3
	%_4 = getelementptr i8, i8* %this, i32 8
	%_5 = bitcast i8* %_4 to i32**
	store i32* %_3, i32** %_5
	%_6 = getelementptr i8, i8* %this, i32 8
	%_7 = bitcast i8* %_6 to i32**
	%_8 = load i32*, i32** %_7
	%_9 = load i32, i32* %r
	%_10 = icmp slt i32 0, 0
	br i1 %_10, label %arr_alloc2, label %arr_alloc3
arr_alloc2:
	call void @throw_oob()
	br label %arr_alloc3
arr_alloc3:
	%_11 = getelementptr i32, i32* %_8, i32 0
	%_12 = load i32, i32* %_11
	%_13 = icmp sle i32 %_12, 0
	br i1 %_13, label %arr_alloc4, label %arr_alloc5
arr_alloc4:
	call void @throw_oob()
	br label %arr_alloc5
arr_alloc5:
	%_14 = add i32 0, 1
	%_15 = getelementptr i32, i32* %_8, i32 %_14
	store i32 %_9, i32* %_15
	%_16 = getelementptr i8, i8* %this, i32 8
	%_17 = bitcast i8* %_16 to i32**
	%_18 = load i32*, i32** %_17
	%_19 = load i32, i32* %g
	%_20 = icmp slt i32 1, 0
	br i1 %_20, label %arr_alloc6, label %arr_alloc7
arr_alloc6:
	call void @throw_oob()
	br label %arr_alloc7
arr_alloc7:
	%_21 = getelementptr i32, i32* %_18, i32 0
	%_22 = load i32, i32* %_21
	%_23 = icmp sle i32 %_22, 1
	br i1 %_23, label %arr_alloc8, label %arr_alloc9
arr_alloc8:
	call void @throw_oob()
	br label %arr_alloc9
arr_alloc9:
	%_24 = add i32 1, 1
	%_25 = getelementptr i32, i32* %_18, i32 %_24
	store i32 %_19, i32* %_25
	%_26 = getelementptr i8, i8* %this, i32 8
	%_27 = bitcast i8* %_26 to i32**
	%_28 = load i32*, i32** %_27
	%_29 = load i32, i32* %b
	%_30 = icmp slt i32 2, 0
	br i1 %_30, label %arr_alloc10, label %arr_alloc11
arr_alloc10:
	call void @throw_oob()
	br label %arr_alloc11
arr_alloc11:
	%_31 = getelementptr i32, i32* %_28, i32 0
	%_32 = load i32, i32* %_31
	%_33 = icmp sle i32 %_32, 2
	br i1 %_33, label %arr_alloc12, label %arr_alloc13
arr_alloc12:
	call void @throw_oob()
	br label %arr_alloc13
arr_alloc13:
	%_34 = add i32 2, 1
	%_35 = getelementptr i32, i32* %_28, i32 %_34
	store i32 %_29, i32* %_35
	ret i1 1
}

define i32* @Color.getRGB(i8* %this) {
	%_0 = getelementptr i8, i8* %this, i32 8
	%_1 = bitcast i8* %_0 to i32**
	%_2 = load i32*, i32** %_1
	ret i32* %_2
}

define i8* @Color.addIn(i8* %this, i8* %.otherColor) {
	%otherColor = alloca i8*
	store i8* %.otherColor, i8** %otherColor
	%otherRGB = alloca i32*
	%_0 = load i8*, i8** %otherColor
	%_1 = bitcast i8* %_0 to i8***
	%_2 = load i8**, i8*** %_1
	%_3 = getelementptr i8*, i8** %_2, i32 2
	%_4 = load i8*, i8** %_3
	%_5 = bitcast i8* %_4 to i32* (i8*)*
	%_6 = call i32* %_5(i8* %_0)
	store i32* %_6, i32** %otherRGB
	%_7 = getelementptr i8, i8* %this, i32 8
	%_8 = bitcast i8* %_7 to i32**
	%_9 = load i32*, i32** %_8
	%_10 = getelementptr i8, i8* %this, i32 8
	%_11 = bitcast i8* %_10 to i32**
	%_12 = load i32*, i32** %_11
	%_13 = icmp slt i32 0, 0
	br i1 %_13, label %arr_alloc0, label %arr_alloc1
arr_alloc0:
	call void @throw_oob()
	br label %arr_alloc1
arr_alloc1:
	%_14 = getelementptr i32, i32* %_12, i32 0
	%_15 = load i32, i32* %_14
	%_16 = icmp sle i32 %_15, 0
	br i1 %_16, label %arr_alloc2, label %arr_alloc3
arr_alloc2:
	call void @throw_oob()
	br label %arr_alloc3
arr_alloc3:
	%_17 = add i32 0, 1
	%_18 = getelementptr i32, i32* %_12, i32 %_17
	%_19 = load i32, i32* %_18
	%_20 = load i32*, i32** %otherRGB
	%_21 = icmp slt i32 0, 0
	br i1 %_21, label %arr_alloc4, label %arr_alloc5
arr_alloc4:
	call void @throw_oob()
	br label %arr_alloc5
arr_alloc5:
	%_22 = getelementptr i32, i32* %_20, i32 0
	%_23 = load i32, i32* %_22
	%_24 = icmp sle i32 %_23, 0
	br i1 %_24, label %arr_alloc6, label %arr_alloc7
arr_alloc6:
	call void @throw_oob()
	br label %arr_alloc7
arr_alloc7:
	%_25 = add i32 0, 1
	%_26 = getelementptr i32, i32* %_20, i32 %_25
	%_27 = load i32, i32* %_26
	%_28 = add i32 %_19, %_27
	%_29 = icmp slt i32 0, 0
	br i1 %_29, label %arr_alloc8, label %arr_alloc9
arr_alloc8:
	call void @throw_oob()
	br label %arr_alloc9
arr_alloc9:
	%_30 = getelementptr i32, i32* %_9, i32 0
	%_31 = load i32, i32* %_30
	%_32 = icmp sle i32 %_31, 0
	br i1 %_32, label %arr_alloc10, label %arr_alloc11
arr_alloc10:
	call void @throw_oob()
	br label %arr_alloc11
arr_alloc11:
	%_33 = add i32 0, 1
	%_34 = getelementptr i32, i32* %_9, i32 %_33
	store i32 %_28, i32* %_34
	%_35 = getelementptr i8, i8* %this, i32 8
	%_36 = bitcast i8* %_35 to i32**
	%_37 = load i32*, i32** %_36
	%_38 = getelementptr i8, i8* %this, i32 8
	%_39 = bitcast i8* %_38 to i32**
	%_40 = load i32*, i32** %_39
	%_41 = icmp slt i32 1, 0
	br i1 %_41, label %arr_alloc12, label %arr_alloc13
arr_alloc12:
	call void @throw_oob()
	br label %arr_alloc13
arr_alloc13:
	%_42 = getelementptr i32, i32* %_40, i32 0
	%_43 = load i32, i32* %_42
	%_44 = icmp sle i32 %_43, 1
	br i1 %_44, label %arr_alloc14, label %arr_alloc15
arr_alloc14:
	call void @throw_oob()
	br label %arr_alloc15
arr_alloc15:
	%_45 = add i32 1, 1
	%_46 = getelementptr i32, i32* %_40, i32 %_45
	%_47 = load i32, i32* %_46
	%_48 = load i32*, i32** %otherRGB
	%_49 = icmp slt i32 1, 0
	br i1 %_49, label %arr_alloc16, label %arr_alloc17
arr_alloc16:
	call void @throw_oob()
	br label %arr_alloc17
arr_alloc17:
	%_50 = getelementptr i32, i32* %_48, i32 0
	%_51 = load i32, i32* %_50
	%_52 = icmp sle i32 %_51, 1
	br i1 %_52, label %arr_alloc18, label %arr_alloc19
arr_alloc18:
	call void @throw_oob()
	br label %arr_alloc19
arr_alloc19:
	%_53 = add i32 1, 1
	%_54 = getelementptr i32, i32* %_48, i32 %_53
	%_55 = load i32, i32* %_54
	%_56 = add i32 %_47, %_55
	%_57 = icmp slt i32 1, 0
	br i1 %_57, label %arr_alloc20, label %arr_alloc21
arr_alloc20:
	call void @throw_oob()
	br label %arr_alloc21
arr_alloc21:
	%_58 = getelementptr i32, i32* %_37, i32 0
	%_59 = load i32, i32* %_58
	%_60 = icmp sle i32 %_59, 1
	br i1 %_60, label %arr_alloc22, label %arr_alloc23
arr_alloc22:
	call void @throw_oob()
	br label %arr_alloc23
arr_alloc23:
	%_61 = add i32 1, 1
	%_62 = getelementptr i32, i32* %_37, i32 %_61
	store i32 %_56, i32* %_62
	%_63 = getelementptr i8, i8* %this, i32 8
	%_64 = bitcast i8* %_63 to i32**
	%_65 = load i32*, i32** %_64
	%_66 = getelementptr i8, i8* %this, i32 8
	%_67 = bitcast i8* %_66 to i32**
	%_68 = load i32*, i32** %_67
	%_69 = icmp slt i32 2, 0
	br i1 %_69, label %arr_alloc24, label %arr_alloc25
arr_alloc24:
	call void @throw_oob()
	br label %arr_alloc25
arr_alloc25:
	%_70 = getelementptr i32, i32* %_68, i32 0
	%_71 = load i32, i32* %_70
	%_72 = icmp sle i32 %_71, 2
	br i1 %_72, label %arr_alloc26, label %arr_alloc27
arr_alloc26:
	call void @throw_oob()
	br label %arr_alloc27
arr_alloc27:
	%_73 = add i32 2, 1
	%_74 = getelementptr i32, i32* %_68, i32 %_73
	%_75 = load i32, i32* %_74
	%_76 = load i32*, i32** %otherRGB
	%_77 = icmp slt i32 2, 0
	br i1 %_77, label %arr_alloc28, label %arr_alloc29
arr_alloc28:
	call void @throw_oob()
	br label %arr_alloc29
arr_alloc29:
	%_78 = getelementptr i32, i32* %_76, i32 0
	%_79 = load i32, i32* %_78
	%_80 = icmp sle i32 %_79, 2
	br i1 %_80, label %arr_alloc30, label %arr_alloc31
arr_alloc30:
	call void @throw_oob()
	br label %arr_alloc31
arr_alloc31:
	%_81 = add i32 2, 1
	%_82 = getelementptr i32, i32* %_76, i32 %_81
	%_83 = load i32, i32* %_82
	%_84 = add i32 %_75, %_83
	%_85 = icmp slt i32 2, 0
	br i1 %_85, label %arr_alloc32, label %arr_alloc33
arr_alloc32:
	call void @throw_oob()
	br label %arr_alloc33
arr_alloc33:
	%_86 = getelementptr i32, i32* %_65, i32 0
	%_87 = load i32, i32* %_86
	%_88 = icmp sle i32 %_87, 2
	br i1 %_88, label %arr_alloc34, label %arr_alloc35
arr_alloc34:
	call void @throw_oob()
	br label %arr_alloc35
arr_alloc35:
	%_89 = add i32 2, 1
	%_90 = getelementptr i32, i32* %_65, i32 %_89
	store i32 %_84, i32* %_90
	ret i8* %this
}

define i8* @Color.addOut(i8* %this, i8* %.otherColor) {
	%otherColor = alloca i8*
	store i8* %.otherColor, i8** %otherColor
	%result = alloca i8*
	%_0 = load i8*, i8** %otherColor
	%_1 = bitcast i8* %_0 to i8***
	%_2 = load i8**, i8*** %_1
	%_3 = getelementptr i8*, i8** %_2, i32 3
	%_4 = load i8*, i8** %_3
	%_5 = bitcast i8* %_4 to i8* (i8*, i8*)*
	%_6 = call i8* %_5(i8* %_0, i8* %this)
	store i8* %_6, i8** %result
	%_7 = load i8*, i8** %otherColor
	ret i8* %_7
}

define i8* @Color.switchTo(i8* %this, i8* %.otherColor) {
	%otherColor = alloca i8*
	store i8* %.otherColor, i8** %otherColor
	store i8* %this, i8** %otherColor
	%_0 = load i8*, i8** %otherColor
	ret i8* %_0
}

define i8* @Color.doubleColor(i8* %this) {
	%doubled = alloca i8*
	%doubledRGB = alloca i32*
	store i8* %this, i8** %doubled
	%_0 = load i8*, i8** %doubled
	%_1 = bitcast i8* %_0 to i8***
	%_2 = load i8**, i8*** %_1
	%_3 = getelementptr i8*, i8** %_2, i32 2
	%_4 = load i8*, i8** %_3
	%_5 = bitcast i8* %_4 to i32* (i8*)*
	%_6 = call i32* %_5(i8* %_0)
	store i32* %_6, i32** %doubledRGB
	%_7 = load i32*, i32** %doubledRGB
	%_8 = load i32*, i32** %doubledRGB
	%_9 = icmp slt i32 0, 0
	br i1 %_9, label %arr_alloc0, label %arr_alloc1
arr_alloc0:
	call void @throw_oob()
	br label %arr_alloc1
arr_alloc1:
	%_10 = getelementptr i32, i32* %_8, i32 0
	%_11 = load i32, i32* %_10
	%_12 = icmp sle i32 %_11, 0
	br i1 %_12, label %arr_alloc2, label %arr_alloc3
arr_alloc2:
	call void @throw_oob()
	br label %arr_alloc3
arr_alloc3:
	%_13 = add i32 0, 1
	%_14 = getelementptr i32, i32* %_8, i32 %_13
	%_15 = load i32, i32* %_14
	%_16 = mul i32 %_15, 2
	%_17 = icmp slt i32 0, 0
	br i1 %_17, label %arr_alloc4, label %arr_alloc5
arr_alloc4:
	call void @throw_oob()
	br label %arr_alloc5
arr_alloc5:
	%_18 = getelementptr i32, i32* %_7, i32 0
	%_19 = load i32, i32* %_18
	%_20 = icmp sle i32 %_19, 0
	br i1 %_20, label %arr_alloc6, label %arr_alloc7
arr_alloc6:
	call void @throw_oob()
	br label %arr_alloc7
arr_alloc7:
	%_21 = add i32 0, 1
	%_22 = getelementptr i32, i32* %_7, i32 %_21
	store i32 %_16, i32* %_22
	%_23 = load i32*, i32** %doubledRGB
	%_24 = load i32*, i32** %doubledRGB
	%_25 = icmp slt i32 1, 0
	br i1 %_25, label %arr_alloc8, label %arr_alloc9
arr_alloc8:
	call void @throw_oob()
	br label %arr_alloc9
arr_alloc9:
	%_26 = getelementptr i32, i32* %_24, i32 0
	%_27 = load i32, i32* %_26
	%_28 = icmp sle i32 %_27, 1
	br i1 %_28, label %arr_alloc10, label %arr_alloc11
arr_alloc10:
	call void @throw_oob()
	br label %arr_alloc11
arr_alloc11:
	%_29 = add i32 1, 1
	%_30 = getelementptr i32, i32* %_24, i32 %_29
	%_31 = load i32, i32* %_30
	%_32 = mul i32 %_31, 2
	%_33 = icmp slt i32 1, 0
	br i1 %_33, label %arr_alloc12, label %arr_alloc13
arr_alloc12:
	call void @throw_oob()
	br label %arr_alloc13
arr_alloc13:
	%_34 = getelementptr i32, i32* %_23, i32 0
	%_35 = load i32, i32* %_34
	%_36 = icmp sle i32 %_35, 1
	br i1 %_36, label %arr_alloc14, label %arr_alloc15
arr_alloc14:
	call void @throw_oob()
	br label %arr_alloc15
arr_alloc15:
	%_37 = add i32 1, 1
	%_38 = getelementptr i32, i32* %_23, i32 %_37
	store i32 %_32, i32* %_38
	%_39 = load i32*, i32** %doubledRGB
	%_40 = load i32*, i32** %doubledRGB
	%_41 = icmp slt i32 2, 0
	br i1 %_41, label %arr_alloc16, label %arr_alloc17
arr_alloc16:
	call void @throw_oob()
	br label %arr_alloc17
arr_alloc17:
	%_42 = getelementptr i32, i32* %_40, i32 0
	%_43 = load i32, i32* %_42
	%_44 = icmp sle i32 %_43, 2
	br i1 %_44, label %arr_alloc18, label %arr_alloc19
arr_alloc18:
	call void @throw_oob()
	br label %arr_alloc19
arr_alloc19:
	%_45 = add i32 2, 1
	%_46 = getelementptr i32, i32* %_40, i32 %_45
	%_47 = load i32, i32* %_46
	%_48 = mul i32 %_47, 2
	%_49 = icmp slt i32 2, 0
	br i1 %_49, label %arr_alloc20, label %arr_alloc21
arr_alloc20:
	call void @throw_oob()
	br label %arr_alloc21
arr_alloc21:
	%_50 = getelementptr i32, i32* %_39, i32 0
	%_51 = load i32, i32* %_50
	%_52 = icmp sle i32 %_51, 2
	br i1 %_52, label %arr_alloc22, label %arr_alloc23
arr_alloc22:
	call void @throw_oob()
	br label %arr_alloc23
arr_alloc23:
	%_53 = add i32 2, 1
	%_54 = getelementptr i32, i32* %_39, i32 %_53
	store i32 %_48, i32* %_54
	%_55 = load i8*, i8** %doubled
	ret i8* %_55
}

define i8* @Color.setColorField(i8* %this) {
	%_0 = getelementptr i8, i8* %this, i32 16
	%_1 = bitcast i8* %_0 to i8**
	store i8* %this, i8** %_1
	ret i8* %this
}

define i8* @Color.getColorField(i8* %this) {
	%_0 = getelementptr i8, i8* %this, i32 16
	%_1 = bitcast i8* %_0 to i8**
	%_2 = load i8*, i8** %_1
	ret i8* %_2
}

define i32 @Color.getSum(i8* %this) {
	%_0 = getelementptr i8, i8* %this, i32 8
	%_1 = bitcast i8* %_0 to i32**
	%_2 = load i32*, i32** %_1
	%_3 = icmp slt i32 0, 0
	br i1 %_3, label %arr_alloc0, label %arr_alloc1
arr_alloc0:
	call void @throw_oob()
	br label %arr_alloc1
arr_alloc1:
	%_4 = getelementptr i32, i32* %_2, i32 0
	%_5 = load i32, i32* %_4
	%_6 = icmp sle i32 %_5, 0
	br i1 %_6, label %arr_alloc2, label %arr_alloc3
arr_alloc2:
	call void @throw_oob()
	br label %arr_alloc3
arr_alloc3:
	%_7 = add i32 0, 1
	%_8 = getelementptr i32, i32* %_2, i32 %_7
	%_9 = load i32, i32* %_8
	%_10 = getelementptr i8, i8* %this, i32 8
	%_11 = bitcast i8* %_10 to i32**
	%_12 = load i32*, i32** %_11
	%_13 = icmp slt i32 1, 0
	br i1 %_13, label %arr_alloc4, label %arr_alloc5
arr_alloc4:
	call void @throw_oob()
	br label %arr_alloc5
arr_alloc5:
	%_14 = getelementptr i32, i32* %_12, i32 0
	%_15 = load i32, i32* %_14
	%_16 = icmp sle i32 %_15, 1
	br i1 %_16, label %arr_alloc6, label %arr_alloc7
arr_alloc6:
	call void @throw_oob()
	br label %arr_alloc7
arr_alloc7:
	%_17 = add i32 1, 1
	%_18 = getelementptr i32, i32* %_12, i32 %_17
	%_19 = load i32, i32* %_18
	%_20 = getelementptr i8, i8* %this, i32 8
	%_21 = bitcast i8* %_20 to i32**
	%_22 = load i32*, i32** %_21
	%_23 = icmp slt i32 2, 0
	br i1 %_23, label %arr_alloc8, label %arr_alloc9
arr_alloc8:
	call void @throw_oob()
	br label %arr_alloc9
arr_alloc9:
	%_24 = getelementptr i32, i32* %_22, i32 0
	%_25 = load i32, i32* %_24
	%_26 = icmp sle i32 %_25, 2
	br i1 %_26, label %arr_alloc10, label %arr_alloc11
arr_alloc10:
	call void @throw_oob()
	br label %arr_alloc11
arr_alloc11:
	%_27 = add i32 2, 1
	%_28 = getelementptr i32, i32* %_22, i32 %_27
	%_29 = load i32, i32* %_28
	%_30 = add i32 %_19, %_29
	%_31 = add i32 %_9, %_30
	ret i32 %_31
}

define i1 @Test.initTest(i8* %this) {
	%c1RGB = alloca i32*
	%c2R = alloca i32
	%c2G = alloca i32
	%c2B = alloca i32
	%placeholder = alloca i1
	%_0 = call i8* @calloc(i32 1, i32 24)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [10 x i8*], [10 x i8*]* @.Color_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	%_3 = getelementptr i8, i8* %this, i32 8
	%_4 = bitcast i8* %_3 to i8**
	store i8* %_0, i8** %_4
	%_5 = call i8* @calloc(i32 1, i32 24)
	%_6 = bitcast i8* %_5 to i8***
	%_7 = getelementptr [10 x i8*], [10 x i8*]* @.Color_vtable, i32 0, i32 0
	store i8** %_7, i8*** %_6
	%_8 = getelementptr i8, i8* %this, i32 16
	%_9 = bitcast i8* %_8 to i8**
	store i8* %_5, i8** %_9
	%_10 = icmp slt i32 3, 0
	br i1 %_10, label %arr_alloc0, label %arr_alloc1
arr_alloc0:
	call void @throw_oob()
	br label %arr_alloc1
arr_alloc1:
	%_11 = add i32 3, 1
	%_12 = call i8* @calloc(i32 4, i32 %_11)
	%_13 = bitcast i8* %_12 to i32*
	store i32 3, i32* %_13
	store i32* %_13, i32** %c1RGB
	%_14 = load i32*, i32** %c1RGB
	%_15 = icmp slt i32 0, 0
	br i1 %_15, label %arr_alloc2, label %arr_alloc3
arr_alloc2:
	call void @throw_oob()
	br label %arr_alloc3
arr_alloc3:
	%_16 = getelementptr i32, i32* %_14, i32 0
	%_17 = load i32, i32* %_16
	%_18 = icmp sle i32 %_17, 0
	br i1 %_18, label %arr_alloc4, label %arr_alloc5
arr_alloc4:
	call void @throw_oob()
	br label %arr_alloc5
arr_alloc5:
	%_19 = add i32 0, 1
	%_20 = getelementptr i32, i32* %_14, i32 %_19
	store i32 10, i32* %_20
	%_21 = load i32*, i32** %c1RGB
	%_22 = icmp slt i32 1, 0
	br i1 %_22, label %arr_alloc6, label %arr_alloc7
arr_alloc6:
	call void @throw_oob()
	br label %arr_alloc7
arr_alloc7:
	%_23 = getelementptr i32, i32* %_21, i32 0
	%_24 = load i32, i32* %_23
	%_25 = icmp sle i32 %_24, 1
	br i1 %_25, label %arr_alloc8, label %arr_alloc9
arr_alloc8:
	call void @throw_oob()
	br label %arr_alloc9
arr_alloc9:
	%_26 = add i32 1, 1
	%_27 = getelementptr i32, i32* %_21, i32 %_26
	store i32 10, i32* %_27
	%_28 = load i32*, i32** %c1RGB
	%_29 = icmp slt i32 2, 0
	br i1 %_29, label %arr_alloc10, label %arr_alloc11
arr_alloc10:
	call void @throw_oob()
	br label %arr_alloc11
arr_alloc11:
	%_30 = getelementptr i32, i32* %_28, i32 0
	%_31 = load i32, i32* %_30
	%_32 = icmp sle i32 %_31, 2
	br i1 %_32, label %arr_alloc12, label %arr_alloc13
arr_alloc12:
	call void @throw_oob()
	br label %arr_alloc13
arr_alloc13:
	%_33 = add i32 2, 1
	%_34 = getelementptr i32, i32* %_28, i32 %_33
	store i32 10, i32* %_34
	%_35 = getelementptr i8, i8* %this, i32 8
	%_36 = bitcast i8* %_35 to i8**
	%_37 = load i8*, i8** %_36
	%_38 = bitcast i8* %_37 to i8***
	%_39 = load i8**, i8*** %_38
	%_40 = getelementptr i8*, i8** %_39, i32 0
	%_41 = load i8*, i8** %_40
	%_42 = bitcast i8* %_41 to i1 (i8*, i32*)*
	%_43 = load i32*, i32** %c1RGB
	%_44 = call i1 %_42(i8* %_37, i32* %_43)
	store i1 %_44, i1* %placeholder
	store i32 20, i32* %c2R
	store i32 20, i32* %c2G
	store i32 20, i32* %c2B
	%_45 = getelementptr i8, i8* %this, i32 16
	%_46 = bitcast i8* %_45 to i8**
	%_47 = load i8*, i8** %_46
	%_48 = bitcast i8* %_47 to i8***
	%_49 = load i8**, i8*** %_48
	%_50 = getelementptr i8*, i8** %_49, i32 1
	%_51 = load i8*, i8** %_50
	%_52 = bitcast i8* %_51 to i1 (i8*, i32, i32, i32)*
	%_53 = load i32, i32* %c2R
	%_54 = load i32, i32* %c2G
	%_55 = load i32, i32* %c2B
	%_56 = call i1 %_52(i8* %_47, i32 %_53, i32 %_54, i32 %_55)
	store i1 %_56, i1* %placeholder
	ret i1 1
}

define i32 @Test.test(i8* %this) {
	%placeholder = alloca i1
	%result = alloca i8*
	%_0 = bitcast i8* %this to i8***
	%_1 = load i8**, i8*** %_0
	%_2 = getelementptr i8*, i8** %_1, i32 0
	%_3 = load i8*, i8** %_2
	%_4 = bitcast i8* %_3 to i1 (i8*)*
	%_5 = call i1 %_4(i8* %this)
	store i1 %_5, i1* %placeholder
	%_6 = getelementptr i8, i8* %this, i32 8
	%_7 = bitcast i8* %_6 to i8**
	%_8 = load i8*, i8** %_7
	%_9 = bitcast i8* %_8 to i8***
	%_10 = load i8**, i8*** %_9
	%_11 = getelementptr i8*, i8** %_10, i32 3
	%_12 = load i8*, i8** %_11
	%_13 = bitcast i8* %_12 to i8* (i8*, i8*)*
	%_14 = getelementptr i8, i8* %this, i32 16
	%_15 = bitcast i8* %_14 to i8**
	%_16 = load i8*, i8** %_15
	%_17 = call i8* %_13(i8* %_8, i8* %_16)
	store i8* %_17, i8** %result
	%_18 = load i8*, i8** %result
	%_19 = bitcast i8* %_18 to i8***
	%_20 = load i8**, i8*** %_19
	%_21 = getelementptr i8*, i8** %_20, i32 9
	%_22 = load i8*, i8** %_21
	%_23 = bitcast i8* %_22 to i32 (i8*)*
	%_24 = call i32 %_23(i8* %_18)
	call void (i32) @print_int(i32 %_24)
	%_25 = getelementptr i8, i8* %this, i32 8
	%_26 = bitcast i8* %_25 to i8**
	%_27 = load i8*, i8** %_26
	%_28 = bitcast i8* %_27 to i8***
	%_29 = load i8**, i8*** %_28
	%_30 = getelementptr i8*, i8** %_29, i32 4
	%_31 = load i8*, i8** %_30
	%_32 = bitcast i8* %_31 to i8* (i8*, i8*)*
	%_33 = getelementptr i8, i8* %this, i32 16
	%_34 = bitcast i8* %_33 to i8**
	%_35 = load i8*, i8** %_34
	%_36 = call i8* %_32(i8* %_27, i8* %_35)
	store i8* %_36, i8** %result
	%_37 = load i8*, i8** %result
	%_38 = bitcast i8* %_37 to i8***
	%_39 = load i8**, i8*** %_38
	%_40 = getelementptr i8*, i8** %_39, i32 9
	%_41 = load i8*, i8** %_40
	%_42 = bitcast i8* %_41 to i32 (i8*)*
	%_43 = call i32 %_42(i8* %_37)
	call void (i32) @print_int(i32 %_43)
	%_44 = getelementptr i8, i8* %this, i32 16
	%_45 = bitcast i8* %_44 to i8**
	%_46 = load i8*, i8** %_45
	%_47 = bitcast i8* %_46 to i8***
	%_48 = load i8**, i8*** %_47
	%_49 = getelementptr i8*, i8** %_48, i32 5
	%_50 = load i8*, i8** %_49
	%_51 = bitcast i8* %_50 to i8* (i8*, i8*)*
	%_52 = getelementptr i8, i8* %this, i32 8
	%_53 = bitcast i8* %_52 to i8**
	%_54 = load i8*, i8** %_53
	%_55 = call i8* %_51(i8* %_46, i8* %_54)
	store i8* %_55, i8** %result
	%_56 = getelementptr i8, i8* %this, i32 8
	%_57 = bitcast i8* %_56 to i8**
	%_58 = load i8*, i8** %_57
	%_59 = bitcast i8* %_58 to i8***
	%_60 = load i8**, i8*** %_59
	%_61 = getelementptr i8*, i8** %_60, i32 9
	%_62 = load i8*, i8** %_61
	%_63 = bitcast i8* %_62 to i32 (i8*)*
	%_64 = call i32 %_63(i8* %_58)
	call void (i32) @print_int(i32 %_64)
	%_65 = getelementptr i8, i8* %this, i32 8
	%_66 = bitcast i8* %_65 to i8**
	%_67 = load i8*, i8** %_66
	%_68 = bitcast i8* %_67 to i8***
	%_69 = load i8**, i8*** %_68
	%_70 = getelementptr i8*, i8** %_69, i32 6
	%_71 = load i8*, i8** %_70
	%_72 = bitcast i8* %_71 to i8* (i8*)*
	%_73 = call i8* %_72(i8* %_67)
	%_74 = getelementptr i8, i8* %this, i32 8
	%_75 = bitcast i8* %_74 to i8**
	store i8* %_73, i8** %_75
	%_76 = getelementptr i8, i8* %this, i32 8
	%_77 = bitcast i8* %_76 to i8**
	%_78 = load i8*, i8** %_77
	%_79 = bitcast i8* %_78 to i8***
	%_80 = load i8**, i8*** %_79
	%_81 = getelementptr i8*, i8** %_80, i32 9
	%_82 = load i8*, i8** %_81
	%_83 = bitcast i8* %_82 to i32 (i8*)*
	%_84 = call i32 %_83(i8* %_78)
	call void (i32) @print_int(i32 %_84)
	%_85 = getelementptr i8, i8* %this, i32 16
	%_86 = bitcast i8* %_85 to i8**
	%_87 = load i8*, i8** %_86
	%_88 = bitcast i8* %_87 to i8***
	%_89 = load i8**, i8*** %_88
	%_90 = getelementptr i8*, i8** %_89, i32 6
	%_91 = load i8*, i8** %_90
	%_92 = bitcast i8* %_91 to i8* (i8*)*
	%_93 = call i8* %_92(i8* %_87)
	store i8* %_93, i8** %result
	%_94 = getelementptr i8, i8* %this, i32 16
	%_95 = bitcast i8* %_94 to i8**
	%_96 = load i8*, i8** %_95
	%_97 = bitcast i8* %_96 to i8***
	%_98 = load i8**, i8*** %_97
	%_99 = getelementptr i8*, i8** %_98, i32 9
	%_100 = load i8*, i8** %_99
	%_101 = bitcast i8* %_100 to i32 (i8*)*
	%_102 = call i32 %_101(i8* %_96)
	call void (i32) @print_int(i32 %_102)
	%_103 = getelementptr i8, i8* %this, i32 8
	%_104 = bitcast i8* %_103 to i8**
	%_105 = load i8*, i8** %_104
	%_106 = bitcast i8* %_105 to i8***
	%_107 = load i8**, i8*** %_106
	%_108 = getelementptr i8*, i8** %_107, i32 7
	%_109 = load i8*, i8** %_108
	%_110 = bitcast i8* %_109 to i8* (i8*)*
	%_111 = call i8* %_110(i8* %_105)
	store i8* %_111, i8** %result
	%_112 = getelementptr i8, i8* %this, i32 8
	%_113 = bitcast i8* %_112 to i8**
	%_114 = load i8*, i8** %_113
	%_115 = bitcast i8* %_114 to i8***
	%_116 = load i8**, i8*** %_115
	%_117 = getelementptr i8*, i8** %_116, i32 8
	%_118 = load i8*, i8** %_117
	%_119 = bitcast i8* %_118 to i8* (i8*)*
	%_120 = call i8* %_119(i8* %_114)
	store i8* %_120, i8** %result
	%_121 = load i8*, i8** %result
	%_122 = bitcast i8* %_121 to i8***
	%_123 = load i8**, i8*** %_122
	%_124 = getelementptr i8*, i8** %_123, i32 9
	%_125 = load i8*, i8** %_124
	%_126 = bitcast i8* %_125 to i32 (i8*)*
	%_127 = call i32 %_126(i8* %_121)
	call void (i32) @print_int(i32 %_127)
	ret i32 0
}

