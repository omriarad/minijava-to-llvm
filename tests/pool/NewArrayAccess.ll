@.Simple_vtable = global [1 x i8*] [i8* bitcast (i32 (i8*)* @Simple.bar to i8*)]

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
	%_2 = getelementptr [1 x i8*], [1 x i8*]* @.Simple_vtable, i32 0, i32 0
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

define i32 @Simple.bar(i8* %this) {
	%x = alloca i32*
	%y = alloca i32
	%_0 = icmp slt i32 2, 0
	br i1 %_0, label %arr_alloc0, label %arr_alloc1
arr_alloc0:
	call void @throw_oob()
	br label %arr_alloc1
arr_alloc1:
	%_1 = add i32 2, 1
	%_2 = call i8* @calloc(i32 4, i32 %_1)
	%_3 = bitcast i8* %_2 to i32*
	store i32 2, i32* %_3
	store i32* %_3, i32** %x
	%_4 = icmp slt i32 2, 0
	br i1 %_4, label %arr_alloc2, label %arr_alloc3
arr_alloc2:
	call void @throw_oob()
	br label %arr_alloc3
arr_alloc3:
	%_5 = add i32 2, 1
	%_6 = call i8* @calloc(i32 4, i32 %_5)
	%_7 = bitcast i8* %_6 to i32*
	store i32 2, i32* %_7
	%_8 = icmp slt i32 1, 0
	br i1 %_8, label %arr_alloc4, label %arr_alloc5
arr_alloc4:
	call void @throw_oob()
	br label %arr_alloc5
arr_alloc5:
	%_9 = getelementptr i32, i32* %_7, i32 0
	%_10 = load i32, i32* %_9
	%_11 = icmp sle i32 %_10, 1
	br i1 %_11, label %arr_alloc6, label %arr_alloc7
arr_alloc6:
	call void @throw_oob()
	br label %arr_alloc7
arr_alloc7:
	%_12 = add i32 1, 1
	%_13 = getelementptr i32, i32* %_7, i32 %_12
	%_14 = load i32, i32* %_13
	store i32 %_14, i32* %y
	%_15 = load i32*, i32** %x
	%_16 = icmp slt i32 0, 0
	br i1 %_16, label %arr_alloc8, label %arr_alloc9
arr_alloc8:
	call void @throw_oob()
	br label %arr_alloc9
arr_alloc9:
	%_17 = getelementptr i32, i32* %_15, i32 0
	%_18 = load i32, i32* %_17
	%_19 = icmp sle i32 %_18, 0
	br i1 %_19, label %arr_alloc10, label %arr_alloc11
arr_alloc10:
	call void @throw_oob()
	br label %arr_alloc11
arr_alloc11:
	%_20 = add i32 0, 1
	%_21 = getelementptr i32, i32* %_15, i32 %_20
	store i32 1, i32* %_21
	%_22 = load i32*, i32** %x
	%_23 = icmp slt i32 1, 0
	br i1 %_23, label %arr_alloc12, label %arr_alloc13
arr_alloc12:
	call void @throw_oob()
	br label %arr_alloc13
arr_alloc13:
	%_24 = getelementptr i32, i32* %_22, i32 0
	%_25 = load i32, i32* %_24
	%_26 = icmp sle i32 %_25, 1
	br i1 %_26, label %arr_alloc14, label %arr_alloc15
arr_alloc14:
	call void @throw_oob()
	br label %arr_alloc15
arr_alloc15:
	%_27 = add i32 1, 1
	%_28 = getelementptr i32, i32* %_22, i32 %_27
	store i32 2, i32* %_28
	%_29 = load i32*, i32** %x
	%_30 = icmp slt i32 0, 0
	br i1 %_30, label %arr_alloc16, label %arr_alloc17
arr_alloc16:
	call void @throw_oob()
	br label %arr_alloc17
arr_alloc17:
	%_31 = getelementptr i32, i32* %_29, i32 0
	%_32 = load i32, i32* %_31
	%_33 = icmp sle i32 %_32, 0
	br i1 %_33, label %arr_alloc18, label %arr_alloc19
arr_alloc18:
	call void @throw_oob()
	br label %arr_alloc19
arr_alloc19:
	%_34 = add i32 0, 1
	%_35 = getelementptr i32, i32* %_29, i32 %_34
	%_36 = load i32, i32* %_35
	%_37 = load i32*, i32** %x
	%_38 = icmp slt i32 1, 0
	br i1 %_38, label %arr_alloc20, label %arr_alloc21
arr_alloc20:
	call void @throw_oob()
	br label %arr_alloc21
arr_alloc21:
	%_39 = getelementptr i32, i32* %_37, i32 0
	%_40 = load i32, i32* %_39
	%_41 = icmp sle i32 %_40, 1
	br i1 %_41, label %arr_alloc22, label %arr_alloc23
arr_alloc22:
	call void @throw_oob()
	br label %arr_alloc23
arr_alloc23:
	%_42 = add i32 1, 1
	%_43 = getelementptr i32, i32* %_37, i32 %_42
	%_44 = load i32, i32* %_43
	%_45 = add i32 %_36, %_44
	call void (i32) @print_int(i32 %_45)
	%_46 = load i32, i32* %y
	call void (i32) @print_int(i32 %_46)
	ret i32 0
}

