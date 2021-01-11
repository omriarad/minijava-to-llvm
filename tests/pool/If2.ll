@.Simple_vtable = global [2 x i8*] [i8* bitcast (i32 (i8*, i32)* @Simple.number to i8*), i8* bitcast (i32 (i8*, i32)* @Simple.test to i8*)]

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
	%_0 = call i8* @calloc(i32 1, i32 9)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [2 x i8*], [2 x i8*]* @.Simple_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	%_3 = bitcast i8* %_0 to i8***
	%_4 = load i8**, i8*** %_3
	%_5 = getelementptr i8*, i8** %_4, i32 1
	%_6 = load i8*, i8** %_5
	%_7 = bitcast i8* %_6 to i32 (i8*, i32)*
	%_8 = call i32 %_7(i8* %_0, i32 7)
	call void (i32) @print_int(i32 %_8)
	ret i32 0
}

define i32 @Simple.number(i8* %this, i32 %.num) {
	%num = alloca i32
	store i32 %.num, i32* %num
	%_0 = load i32, i32* %num
	%_1 = mul i32 %_0, 2
	ret i32 %_1
}

define i32 @Simple.test(i8* %this, i32 %.d) {
	%d = alloca i32
	store i32 %.d, i32* %d
	%a = alloca i32
	%b = alloca i32
	%c = alloca i32
	%s = alloca i8*
	store i32 1, i32* %a
	store i32 2, i32* %b
	store i32 3, i32* %c
	%_0 = call i8* @calloc(i32 1, i32 9)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [2 x i8*], [2 x i8*]* @.Simple_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	store i8* %_0, i8** %s
	%_3 = getelementptr i8, i8* %this, i32 8
	%_4 = bitcast i8* %_3 to i1*
	%_5 = load i1, i1* %_4
	%_6 = sub i1 1, %_5
	br label %andcond0
andcond0:
	br i1 %_6, label %andcond1, label %andcond3
andcond1:
	%_7 = load i32, i32* %a
	%_8 = load i32, i32* %b
	%_9 = icmp slt i32 %_7, %_8
	br label %andcond4
andcond4:
	br i1 %_9, label %andcond5, label %andcond7
andcond5:
	%_10 = load i32, i32* %b
	%_11 = load i32, i32* %c
	%_12 = icmp slt i32 %_10, %_11
	br label %andcond8
andcond8:
	br i1 %_12, label %andcond9, label %andcond11
andcond9:
	%_13 = load i32, i32* %d
	%_14 = load i32, i32* %c
	%_15 = icmp slt i32 %_13, %_14
	br label %andcond12
andcond12:
	br i1 %_15, label %andcond13, label %andcond15
andcond13:
	%_16 = load i8*, i8** %s
	%_17 = bitcast i8* %_16 to i8***
	%_18 = load i8**, i8*** %_17
	%_19 = getelementptr i8*, i8** %_18, i32 0
	%_20 = load i8*, i8** %_19
	%_21 = bitcast i8* %_20 to i32 (i8*, i32)*
	%_22 = load i32, i32* %b
	%_23 = call i32 %_21(i8* %_16, i32 %_22)
	%_24 = load i32, i32* %b
	%_25 = icmp slt i32 %_23, %_24
	%_26 = sub i1 1, %_25
	br label %andcond14
andcond14:
	br label %andcond15
andcond15:
	%_27 = phi i1 [0, %andcond12], [%_26, %andcond14]
	br label %andcond10
andcond10:
	br label %andcond11
andcond11:
	%_28 = phi i1 [0, %andcond8], [%_27, %andcond10]
	br label %andcond6
andcond6:
	br label %andcond7
andcond7:
	%_29 = phi i1 [0, %andcond4], [%_28, %andcond6]
	br label %andcond2
andcond2:
	br label %andcond3
andcond3:
	%_30 = phi i1 [0, %andcond0], [%_29, %andcond2]
	br i1 %_30, label %if16, label %if17
if16:
	call void (i32) @print_int(i32 1)
	br label %if18
if17:
	call void (i32) @print_int(i32 0)
	br label %if18
if18:
	ret i32 5
}

