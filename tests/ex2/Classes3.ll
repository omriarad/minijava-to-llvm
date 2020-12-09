@.Test_vtable = global [1 x i8*] [i8* bitcast (i32 (i8*)* @Test.test to i8*)]

@.A_vtable = global [1 x i8*] [i8* bitcast (i32 (i8*)* @A.init to i8*)]

@.B_vtable = global [1 x i8*] [i8* bitcast (i32 (i8*)* @B.init to i8*)]

@.C_vtable = global [2 x i8*] [i8* bitcast (i32 (i8*)* @C.init to i8*), i8* bitcast (i32 (i8*, i1)* @C.getNumber to i8*)]

@.D_vtable = global [2 x i8*] [i8* bitcast (i32 (i8*)* @D.init to i8*), i8* bitcast (i32 (i8*, i1)* @D.getNumber to i8*)]

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
	%_0 = call i8* @calloc(i32 1, i32 8)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [1 x i8*], [1 x i8*]* @.Test_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	%_3 = bitcast i8* %_0 to i8***
	%_4 = load i8**, i8*** %_3
	%_5 = getelementptr i8*, i8** %_4, i32 0
	%_6 = load i8*, i8** %_5
	%_7 = bitcast i8* %_6 to i32 (i8*)*
	%_8 = call i32 %_7(i8* %_0)
	call void (i32) @print_int(i32 %_8)
	ret i32 0
}

define i32 @Test.test(i8* %this) {
	%a = alloca i8*
	%b = alloca i8*
	%c = alloca i8*
	%d = alloca i8*
	%_0 = call i8* @calloc(i32 1, i32 13)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [1 x i8*], [1 x i8*]* @.A_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	store i8* %_0, i8** %a
	%_3 = call i8* @calloc(i32 1, i32 21)
	%_4 = bitcast i8* %_3 to i8***
	%_5 = getelementptr [1 x i8*], [1 x i8*]* @.B_vtable, i32 0, i32 0
	store i8** %_5, i8*** %_4
	store i8* %_3, i8** %b
	%_6 = call i8* @calloc(i32 1, i32 29)
	%_7 = bitcast i8* %_6 to i8***
	%_8 = getelementptr [2 x i8*], [2 x i8*]* @.C_vtable, i32 0, i32 0
	store i8** %_8, i8*** %_7
	store i8* %_6, i8** %c
	%_9 = call i8* @calloc(i32 1, i32 29)
	%_10 = bitcast i8* %_9 to i8***
	%_11 = getelementptr [2 x i8*], [2 x i8*]* @.D_vtable, i32 0, i32 0
	store i8** %_11, i8*** %_10
	store i8* %_9, i8** %d
	%_12 = load i8*, i8** %a
	%_13 = bitcast i8* %_12 to i8***
	%_14 = load i8**, i8*** %_13
	%_15 = getelementptr i8*, i8** %_14, i32 0
	%_16 = load i8*, i8** %_15
	%_17 = bitcast i8* %_16 to i32 (i8*)*
	%_18 = call i32 %_17(i8* %_12)
	call void (i32) @print_int(i32 %_18)
	%_19 = load i8*, i8** %b
	%_20 = bitcast i8* %_19 to i8***
	%_21 = load i8**, i8*** %_20
	%_22 = getelementptr i8*, i8** %_21, i32 0
	%_23 = load i8*, i8** %_22
	%_24 = bitcast i8* %_23 to i32 (i8*)*
	%_25 = call i32 %_24(i8* %_19)
	call void (i32) @print_int(i32 %_25)
	%_26 = load i8*, i8** %c
	%_27 = bitcast i8* %_26 to i8***
	%_28 = load i8**, i8*** %_27
	%_29 = getelementptr i8*, i8** %_28, i32 0
	%_30 = load i8*, i8** %_29
	%_31 = bitcast i8* %_30 to i32 (i8*)*
	%_32 = call i32 %_31(i8* %_26)
	call void (i32) @print_int(i32 %_32)
	%_33 = load i8*, i8** %d
	%_34 = bitcast i8* %_33 to i8***
	%_35 = load i8**, i8*** %_34
	%_36 = getelementptr i8*, i8** %_35, i32 0
	%_37 = load i8*, i8** %_36
	%_38 = bitcast i8* %_37 to i32 (i8*)*
	%_39 = call i32 %_38(i8* %_33)
	call void (i32) @print_int(i32 %_39)
	ret i32 0
}

define i32 @A.init(i8* %this) {
	%_0 = getelementptr i8, i8* %this, i32 8
	%_1 = bitcast i8* %_0 to i32*
	store i32 1, i32* %_1
	%_2 = getelementptr i8, i8* %this, i32 12
	%_3 = bitcast i8* %_2 to i1*
	store i1 1, i1* %_3
	%_4 = getelementptr i8, i8* %this, i32 8
	%_5 = bitcast i8* %_4 to i32*
	%_6 = load i32, i32* %_5
	ret i32 %_6
}

define i32 @B.init(i8* %this) {
	%_0 = getelementptr i8, i8* %this, i32 8
	%_1 = bitcast i8* %_0 to i32*
	store i32 2, i32* %_1
	%_2 = getelementptr i8, i8* %this, i32 12
	%_3 = bitcast i8* %_2 to i1*
	store i1 1, i1* %_3
	%_4 = icmp slt i32 2, 0
	br i1 %_4, label %arr_alloc0, label %arr_alloc1
arr_alloc0:
	call void @throw_oob()
	br label %arr_alloc1
arr_alloc1:
	%_5 = add i32 2, 1
	%_6 = call i8* @calloc(i32 4, i32 %_5)
	%_7 = bitcast i8* %_6 to i32*
	store i32 2, i32* %_7
	%_8 = getelementptr i8, i8* %this, i32 13
	%_9 = bitcast i8* %_8 to i32**
	store i32* %_7, i32** %_9
	%_10 = getelementptr i8, i8* %this, i32 13
	%_11 = bitcast i8* %_10 to i32**
	%_12 = load i32*, i32** %_11
	%_13 = icmp slt i32 0, 0
	br i1 %_13, label %arr_alloc2, label %arr_alloc3
arr_alloc2:
	call void @throw_oob()
	br label %arr_alloc3
arr_alloc3:
	%_14 = getelementptr i32, i32* %_12, i32 0
	%_15 = load i32, i32* %_14
	%_16 = icmp sle i32 %_15, 0
	br i1 %_16, label %arr_alloc4, label %arr_alloc5
arr_alloc4:
	call void @throw_oob()
	br label %arr_alloc5
arr_alloc5:
	%_17 = add i32 0, 1
	%_18 = getelementptr i32, i32* %_12, i32 %_17
	store i32 3, i32* %_18
	%_19 = getelementptr i8, i8* %this, i32 13
	%_20 = bitcast i8* %_19 to i32**
	%_21 = load i32*, i32** %_20
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
	store i32 4, i32* %_27
	%_28 = getelementptr i8, i8* %this, i32 8
	%_29 = bitcast i8* %_28 to i32*
	%_30 = load i32, i32* %_29
	%_31 = getelementptr i8, i8* %this, i32 13
	%_32 = bitcast i8* %_31 to i32**
	%_33 = load i32*, i32** %_32
	%_34 = icmp slt i32 0, 0
	br i1 %_34, label %arr_alloc10, label %arr_alloc11
arr_alloc10:
	call void @throw_oob()
	br label %arr_alloc11
arr_alloc11:
	%_35 = getelementptr i32, i32* %_33, i32 0
	%_36 = load i32, i32* %_35
	%_37 = icmp sle i32 %_36, 0
	br i1 %_37, label %arr_alloc12, label %arr_alloc13
arr_alloc12:
	call void @throw_oob()
	br label %arr_alloc13
arr_alloc13:
	%_38 = add i32 0, 1
	%_39 = getelementptr i32, i32* %_33, i32 %_38
	%_40 = load i32, i32* %_39
	%_41 = getelementptr i8, i8* %this, i32 13
	%_42 = bitcast i8* %_41 to i32**
	%_43 = load i32*, i32** %_42
	%_44 = icmp slt i32 1, 0
	br i1 %_44, label %arr_alloc14, label %arr_alloc15
arr_alloc14:
	call void @throw_oob()
	br label %arr_alloc15
arr_alloc15:
	%_45 = getelementptr i32, i32* %_43, i32 0
	%_46 = load i32, i32* %_45
	%_47 = icmp sle i32 %_46, 1
	br i1 %_47, label %arr_alloc16, label %arr_alloc17
arr_alloc16:
	call void @throw_oob()
	br label %arr_alloc17
arr_alloc17:
	%_48 = add i32 1, 1
	%_49 = getelementptr i32, i32* %_43, i32 %_48
	%_50 = load i32, i32* %_49
	%_51 = add i32 %_40, %_50
	%_52 = add i32 %_30, %_51
	ret i32 %_52
}

define i32 @C.init(i8* %this) {
	%dNum = alloca i32
	%_0 = getelementptr i8, i8* %this, i32 8
	%_1 = bitcast i8* %_0 to i32*
	store i32 5, i32* %_1
	%_2 = getelementptr i8, i8* %this, i32 12
	%_3 = bitcast i8* %_2 to i1*
	store i1 1, i1* %_3
	%_4 = icmp slt i32 2, 0
	br i1 %_4, label %arr_alloc0, label %arr_alloc1
arr_alloc0:
	call void @throw_oob()
	br label %arr_alloc1
arr_alloc1:
	%_5 = add i32 2, 1
	%_6 = call i8* @calloc(i32 4, i32 %_5)
	%_7 = bitcast i8* %_6 to i32*
	store i32 2, i32* %_7
	%_8 = getelementptr i8, i8* %this, i32 13
	%_9 = bitcast i8* %_8 to i32**
	store i32* %_7, i32** %_9
	%_10 = getelementptr i8, i8* %this, i32 13
	%_11 = bitcast i8* %_10 to i32**
	%_12 = load i32*, i32** %_11
	%_13 = icmp slt i32 0, 0
	br i1 %_13, label %arr_alloc2, label %arr_alloc3
arr_alloc2:
	call void @throw_oob()
	br label %arr_alloc3
arr_alloc3:
	%_14 = getelementptr i32, i32* %_12, i32 0
	%_15 = load i32, i32* %_14
	%_16 = icmp sle i32 %_15, 0
	br i1 %_16, label %arr_alloc4, label %arr_alloc5
arr_alloc4:
	call void @throw_oob()
	br label %arr_alloc5
arr_alloc5:
	%_17 = add i32 0, 1
	%_18 = getelementptr i32, i32* %_12, i32 %_17
	store i32 6, i32* %_18
	%_19 = getelementptr i8, i8* %this, i32 13
	%_20 = bitcast i8* %_19 to i32**
	%_21 = load i32*, i32** %_20
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
	store i32 7, i32* %_27
	%_28 = call i8* @calloc(i32 1, i32 29)
	%_29 = bitcast i8* %_28 to i8***
	%_30 = getelementptr [2 x i8*], [2 x i8*]* @.D_vtable, i32 0, i32 0
	store i8** %_30, i8*** %_29
	%_31 = getelementptr i8, i8* %this, i32 21
	%_32 = bitcast i8* %_31 to i8**
	store i8* %_28, i8** %_32
	%_33 = getelementptr i8, i8* %this, i32 21
	%_34 = bitcast i8* %_33 to i8**
	%_35 = load i8*, i8** %_34
	%_36 = bitcast i8* %_35 to i8***
	%_37 = load i8**, i8*** %_36
	%_38 = getelementptr i8*, i8** %_37, i32 1
	%_39 = load i8*, i8** %_38
	%_40 = bitcast i8* %_39 to i32 (i8*, i1)*
	%_41 = getelementptr i8, i8* %this, i32 12
	%_42 = bitcast i8* %_41 to i1*
	%_43 = load i1, i1* %_42
	%_44 = call i32 %_40(i8* %_35, i1 %_43)
	store i32 %_44, i32* %dNum
	%_45 = getelementptr i8, i8* %this, i32 8
	%_46 = bitcast i8* %_45 to i32*
	%_47 = load i32, i32* %_46
	%_48 = getelementptr i8, i8* %this, i32 13
	%_49 = bitcast i8* %_48 to i32**
	%_50 = load i32*, i32** %_49
	%_51 = icmp slt i32 0, 0
	br i1 %_51, label %arr_alloc10, label %arr_alloc11
arr_alloc10:
	call void @throw_oob()
	br label %arr_alloc11
arr_alloc11:
	%_52 = getelementptr i32, i32* %_50, i32 0
	%_53 = load i32, i32* %_52
	%_54 = icmp sle i32 %_53, 0
	br i1 %_54, label %arr_alloc12, label %arr_alloc13
arr_alloc12:
	call void @throw_oob()
	br label %arr_alloc13
arr_alloc13:
	%_55 = add i32 0, 1
	%_56 = getelementptr i32, i32* %_50, i32 %_55
	%_57 = load i32, i32* %_56
	%_58 = getelementptr i8, i8* %this, i32 13
	%_59 = bitcast i8* %_58 to i32**
	%_60 = load i32*, i32** %_59
	%_61 = icmp slt i32 1, 0
	br i1 %_61, label %arr_alloc14, label %arr_alloc15
arr_alloc14:
	call void @throw_oob()
	br label %arr_alloc15
arr_alloc15:
	%_62 = getelementptr i32, i32* %_60, i32 0
	%_63 = load i32, i32* %_62
	%_64 = icmp sle i32 %_63, 1
	br i1 %_64, label %arr_alloc16, label %arr_alloc17
arr_alloc16:
	call void @throw_oob()
	br label %arr_alloc17
arr_alloc17:
	%_65 = add i32 1, 1
	%_66 = getelementptr i32, i32* %_60, i32 %_65
	%_67 = load i32, i32* %_66
	%_68 = load i32, i32* %dNum
	%_69 = add i32 %_67, %_68
	%_70 = add i32 %_57, %_69
	%_71 = add i32 %_47, %_70
	ret i32 %_71
}

define i32 @C.getNumber(i8* %this, i1 %.bool) {
	%bool = alloca i1
	store i1 %.bool, i1* %bool
	%res = alloca i32
	%_0 = load i1, i1* %bool
	br i1 %_0, label %if0, label %if1
if0:
	store i32 13, i32* %res
	br label %if2
if1:
	store i32 14, i32* %res
	br label %if2
if2:
	%_1 = load i32, i32* %res
	ret i32 %_1
}

define i32 @D.init(i8* %this) {
	%cNum = alloca i32
	%_0 = getelementptr i8, i8* %this, i32 8
	%_1 = bitcast i8* %_0 to i32*
	store i32 10, i32* %_1
	%_2 = getelementptr i8, i8* %this, i32 12
	%_3 = bitcast i8* %_2 to i1*
	store i1 0, i1* %_3
	%_4 = icmp slt i32 2, 0
	br i1 %_4, label %arr_alloc0, label %arr_alloc1
arr_alloc0:
	call void @throw_oob()
	br label %arr_alloc1
arr_alloc1:
	%_5 = add i32 2, 1
	%_6 = call i8* @calloc(i32 4, i32 %_5)
	%_7 = bitcast i8* %_6 to i32*
	store i32 2, i32* %_7
	%_8 = getelementptr i8, i8* %this, i32 13
	%_9 = bitcast i8* %_8 to i32**
	store i32* %_7, i32** %_9
	%_10 = getelementptr i8, i8* %this, i32 13
	%_11 = bitcast i8* %_10 to i32**
	%_12 = load i32*, i32** %_11
	%_13 = icmp slt i32 0, 0
	br i1 %_13, label %arr_alloc2, label %arr_alloc3
arr_alloc2:
	call void @throw_oob()
	br label %arr_alloc3
arr_alloc3:
	%_14 = getelementptr i32, i32* %_12, i32 0
	%_15 = load i32, i32* %_14
	%_16 = icmp sle i32 %_15, 0
	br i1 %_16, label %arr_alloc4, label %arr_alloc5
arr_alloc4:
	call void @throw_oob()
	br label %arr_alloc5
arr_alloc5:
	%_17 = add i32 0, 1
	%_18 = getelementptr i32, i32* %_12, i32 %_17
	store i32 11, i32* %_18
	%_19 = getelementptr i8, i8* %this, i32 13
	%_20 = bitcast i8* %_19 to i32**
	%_21 = load i32*, i32** %_20
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
	store i32 12, i32* %_27
	%_28 = call i8* @calloc(i32 1, i32 29)
	%_29 = bitcast i8* %_28 to i8***
	%_30 = getelementptr [2 x i8*], [2 x i8*]* @.C_vtable, i32 0, i32 0
	store i8** %_30, i8*** %_29
	%_31 = getelementptr i8, i8* %this, i32 21
	%_32 = bitcast i8* %_31 to i8**
	store i8* %_28, i8** %_32
	%_33 = getelementptr i8, i8* %this, i32 21
	%_34 = bitcast i8* %_33 to i8**
	%_35 = load i8*, i8** %_34
	%_36 = bitcast i8* %_35 to i8***
	%_37 = load i8**, i8*** %_36
	%_38 = getelementptr i8*, i8** %_37, i32 1
	%_39 = load i8*, i8** %_38
	%_40 = bitcast i8* %_39 to i32 (i8*, i1)*
	%_41 = getelementptr i8, i8* %this, i32 12
	%_42 = bitcast i8* %_41 to i1*
	%_43 = load i1, i1* %_42
	%_44 = call i32 %_40(i8* %_35, i1 %_43)
	store i32 %_44, i32* %cNum
	%_45 = getelementptr i8, i8* %this, i32 8
	%_46 = bitcast i8* %_45 to i32*
	%_47 = load i32, i32* %_46
	%_48 = getelementptr i8, i8* %this, i32 13
	%_49 = bitcast i8* %_48 to i32**
	%_50 = load i32*, i32** %_49
	%_51 = icmp slt i32 0, 0
	br i1 %_51, label %arr_alloc10, label %arr_alloc11
arr_alloc10:
	call void @throw_oob()
	br label %arr_alloc11
arr_alloc11:
	%_52 = getelementptr i32, i32* %_50, i32 0
	%_53 = load i32, i32* %_52
	%_54 = icmp sle i32 %_53, 0
	br i1 %_54, label %arr_alloc12, label %arr_alloc13
arr_alloc12:
	call void @throw_oob()
	br label %arr_alloc13
arr_alloc13:
	%_55 = add i32 0, 1
	%_56 = getelementptr i32, i32* %_50, i32 %_55
	%_57 = load i32, i32* %_56
	%_58 = getelementptr i8, i8* %this, i32 13
	%_59 = bitcast i8* %_58 to i32**
	%_60 = load i32*, i32** %_59
	%_61 = icmp slt i32 1, 0
	br i1 %_61, label %arr_alloc14, label %arr_alloc15
arr_alloc14:
	call void @throw_oob()
	br label %arr_alloc15
arr_alloc15:
	%_62 = getelementptr i32, i32* %_60, i32 0
	%_63 = load i32, i32* %_62
	%_64 = icmp sle i32 %_63, 1
	br i1 %_64, label %arr_alloc16, label %arr_alloc17
arr_alloc16:
	call void @throw_oob()
	br label %arr_alloc17
arr_alloc17:
	%_65 = add i32 1, 1
	%_66 = getelementptr i32, i32* %_60, i32 %_65
	%_67 = load i32, i32* %_66
	%_68 = load i32, i32* %cNum
	%_69 = add i32 %_67, %_68
	%_70 = add i32 %_57, %_69
	%_71 = add i32 %_47, %_70
	ret i32 %_71
}

define i32 @D.getNumber(i8* %this, i1 %.bool) {
	%bool = alloca i1
	store i1 %.bool, i1* %bool
	%res = alloca i32
	%_0 = load i1, i1* %bool
	br i1 %_0, label %if0, label %if1
if0:
	store i32 8, i32* %res
	br label %if2
if1:
	store i32 9, i32* %res
	br label %if2
if2:
	%_1 = load i32, i32* %res
	ret i32 %_1
}

