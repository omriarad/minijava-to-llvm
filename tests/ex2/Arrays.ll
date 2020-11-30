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
	%_0 = call i8* @calloc(i32 8, i32 8)
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
	%_4 = load i32*, i32** %x
	%_5 = icmp slt i32 0, 0
	br i1 %_5, label %arr_alloc2, label %arr_alloc3
arr_alloc2:
	call void @throw_oob()
	br label %arr_alloc3
arr_alloc3:
	%_6 = getelementptr i32, i32* %_4, i32 0
	%_7 = load i32, i32* %_6
	%_8 = icmp sle i32 %_7, 0
	br i1 %_8, label %arr_alloc4, label %arr_alloc5
arr_alloc4:
	call void @throw_oob()
	br label %arr_alloc5
arr_alloc5:
	%_9 = add i32 0, 1
	%_10 = getelementptr i32, i32* %_4, i32 %_9
	store i32 1, i32* %_10
	%_11 = load i32*, i32** %x
	%_12 = icmp slt i32 1, 0
	br i1 %_12, label %arr_alloc6, label %arr_alloc7
arr_alloc6:
	call void @throw_oob()
	br label %arr_alloc7
arr_alloc7:
	%_13 = getelementptr i32, i32* %_11, i32 0
	%_14 = load i32, i32* %_13
	%_15 = icmp sle i32 %_14, 1
	br i1 %_15, label %arr_alloc8, label %arr_alloc9
arr_alloc8:
	call void @throw_oob()
	br label %arr_alloc9
arr_alloc9:
	%_16 = add i32 1, 1
	%_17 = getelementptr i32, i32* %_11, i32 %_16
	store i32 2, i32* %_17
	%_18 = load i32*, i32** %x
	%_19 = icmp slt i32 0, 0
	br i1 %_19, label %arr_alloc10, label %arr_alloc11
arr_alloc10:
	call void @throw_oob()
	br label %arr_alloc11
arr_alloc11:
	%_20 = getelementptr i32, i32* %_18, i32 0
	%_21 = load i32, i32* %_20
	%_22 = icmp sle i32 %_21, 0
	br i1 %_22, label %arr_alloc12, label %arr_alloc13
arr_alloc12:
	call void @throw_oob()
	br label %arr_alloc13
arr_alloc13:
	%_23 = add i32 0, 1
	%_24 = getelementptr i32, i32* %_18, i32 %_23
	%_25 = load i32, i32* %_24
	%_26 = load i32*, i32** %x
	%_27 = icmp slt i32 1, 0
	br i1 %_27, label %arr_alloc14, label %arr_alloc15
arr_alloc14:
	call void @throw_oob()
	br label %arr_alloc15
arr_alloc15:
	%_28 = getelementptr i32, i32* %_26, i32 0
	%_29 = load i32, i32* %_28
	%_30 = icmp sle i32 %_29, 1
	br i1 %_30, label %arr_alloc16, label %arr_alloc17
arr_alloc16:
	call void @throw_oob()
	br label %arr_alloc17
arr_alloc17:
	%_31 = add i32 1, 1
	%_32 = getelementptr i32, i32* %_26, i32 %_31
	%_33 = load i32, i32* %_32
	%_34 = add i32 %_25, %_33
	call void (i32) @print_int(i32 %_34)
	ret i32 0
}

