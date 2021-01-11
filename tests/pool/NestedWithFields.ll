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
	%_0 = call i8* @calloc(i32 1, i32 10)
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
	%c = alloca i1
	%x = alloca i32
	%i = alloca i32
	%_0 = getelementptr i8, i8* %this, i32 8
	%_1 = bitcast i8* %_0 to i1*
	store i1 1, i1* %_1
	%_2 = getelementptr i8, i8* %this, i32 9
	%_3 = bitcast i8* %_2 to i1*
	store i1 1, i1* %_3
	store i1 1, i1* %c
	store i32 0, i32* %x
	br label %while0
while0:
	%_4 = load i1, i1* %c
	br label %andcond3
andcond3:
	br i1 %_4, label %andcond4, label %andcond6
andcond4:
	br label %andcond5
andcond5:
	br label %andcond6
andcond6:
	%_5 = phi i1 [0, %andcond3], [1, %andcond5]
	br i1 %_5, label %while1, label %while2
while1:
	store i32 0, i32* %i
	br label %while7
while7:
	%_6 = load i32, i32* %i
	%_7 = icmp slt i32 %_6, 10
	br i1 %_7, label %while8, label %while9
while8:
	%_8 = getelementptr i8, i8* %this, i32 8
	%_9 = bitcast i8* %_8 to i1*
	%_10 = load i1, i1* %_9
	br label %andcond10
andcond10:
	br i1 %_10, label %andcond11, label %andcond13
andcond11:
	%_11 = getelementptr i8, i8* %this, i32 9
	%_12 = bitcast i8* %_11 to i1*
	%_13 = load i1, i1* %_12
	br label %andcond12
andcond12:
	br label %andcond13
andcond13:
	%_14 = phi i1 [0, %andcond10], [%_13, %andcond12]
	br label %andcond14
andcond14:
	br i1 %_14, label %andcond15, label %andcond17
andcond15:
	%_15 = load i1, i1* %c
	br label %andcond16
andcond16:
	br label %andcond17
andcond17:
	%_16 = phi i1 [0, %andcond14], [%_15, %andcond16]
	br i1 %_16, label %if18, label %if19
if18:
	%_17 = load i32, i32* %x
	%_18 = add i32 %_17, 1
	store i32 %_18, i32* %x
	br label %if20
if19:
	%_19 = getelementptr i8, i8* %this, i32 9
	%_20 = bitcast i8* %_19 to i1*
	%_21 = load i1, i1* %_20
	br i1 %_21, label %if21, label %if22
if21:
	%_22 = load i32, i32* %x
	%_23 = add i32 %_22, 100
	store i32 %_23, i32* %x
	br label %if23
if22:
	%_24 = load i32, i32* %x
	%_25 = add i32 %_24, 10000
	store i32 %_25, i32* %x
	br label %if23
if23:
	br label %if20
if20:
	%_26 = load i32, i32* %i
	%_27 = add i32 %_26, 1
	store i32 %_27, i32* %i
	br label %while7
while9:
	%_28 = getelementptr i8, i8* %this, i32 8
	%_29 = bitcast i8* %_28 to i1*
	%_30 = load i1, i1* %_29
	br label %andcond24
andcond24:
	br i1 %_30, label %andcond25, label %andcond27
andcond25:
	%_31 = getelementptr i8, i8* %this, i32 9
	%_32 = bitcast i8* %_31 to i1*
	%_33 = load i1, i1* %_32
	br label %andcond26
andcond26:
	br label %andcond27
andcond27:
	%_34 = phi i1 [0, %andcond24], [%_33, %andcond26]
	br i1 %_34, label %if28, label %if29
if28:
	%_35 = getelementptr i8, i8* %this, i32 8
	%_36 = bitcast i8* %_35 to i1*
	store i1 0, i1* %_36
	br label %if30
if29:
	%_37 = getelementptr i8, i8* %this, i32 9
	%_38 = bitcast i8* %_37 to i1*
	%_39 = load i1, i1* %_38
	br label %andcond31
andcond31:
	br i1 %_39, label %andcond32, label %andcond34
andcond32:
	%_40 = load i1, i1* %c
	br label %andcond33
andcond33:
	br label %andcond34
andcond34:
	%_41 = phi i1 [0, %andcond31], [%_40, %andcond33]
	br i1 %_41, label %if35, label %if36
if35:
	%_42 = getelementptr i8, i8* %this, i32 9
	%_43 = bitcast i8* %_42 to i1*
	store i1 0, i1* %_43
	br label %if37
if36:
	store i1 0, i1* %c
	br label %if37
if37:
	br label %if30
if30:
	br label %while0
while2:
	%_44 = load i32, i32* %x
	call void (i32) @print_int(i32 %_44)
	ret i32 0
}

