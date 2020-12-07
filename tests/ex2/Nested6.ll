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
	%a = alloca i1
	%b = alloca i1
	%c = alloca i1
	%x = alloca i32
	%i = alloca i32
	store i1 1, i1* %a
	store i1 1, i1* %b
	store i1 1, i1* %c
	store i32 0, i32* %x
	br label %while0
while0:
	%_0 = load i1, i1* %c
	br label %andcond3
andcond3:
	br i1 %_0, label %andcond4, label %andcond6
andcond4:
	br label %andcond5
andcond5:
	br label %andcond6
andcond6:
	%_1 = phi i1 [0, %andcond3], [1, %andcond5]
	br i1 %_1, label %while1, label %while2
while1:
	store i32 0, i32* %i
	br label %while7
while7:
	%_2 = load i32, i32* %i
	%_3 = icmp slt i32 %_2, 10
	br i1 %_3, label %while8, label %while9
while8:
	%_4 = load i1, i1* %a
	br label %andcond10
andcond10:
	br i1 %_4, label %andcond11, label %andcond13
andcond11:
	%_5 = load i1, i1* %b
	br label %andcond14
andcond14:
	br i1 %_5, label %andcond15, label %andcond17
andcond15:
	%_6 = load i1, i1* %c
	br label %andcond18
andcond18:
	br i1 %_6, label %andcond19, label %andcond21
andcond19:
	br label %andcond22
andcond22:
	br i1 1, label %andcond23, label %andcond25
andcond23:
	%_7 = load i1, i1* %a
	br label %andcond24
andcond24:
	br label %andcond25
andcond25:
	%_8 = phi i1 [0, %andcond22], [%_7, %andcond24]
	br label %andcond20
andcond20:
	br label %andcond21
andcond21:
	%_9 = phi i1 [0, %andcond18], [%_8, %andcond20]
	br label %andcond16
andcond16:
	br label %andcond17
andcond17:
	%_10 = phi i1 [0, %andcond14], [%_9, %andcond16]
	br label %andcond12
andcond12:
	br label %andcond13
andcond13:
	%_11 = phi i1 [0, %andcond10], [%_10, %andcond12]
	br i1 %_11, label %if26, label %if27
if26:
	%_12 = load i32, i32* %x
	%_13 = add i32 %_12, 1
	store i32 %_13, i32* %x
	br label %if28
if27:
	%_14 = load i1, i1* %b
	br i1 %_14, label %if29, label %if30
if29:
	%_15 = load i32, i32* %x
	%_16 = add i32 %_15, 100
	store i32 %_16, i32* %x
	br label %if31
if30:
	%_17 = load i32, i32* %x
	%_18 = add i32 %_17, 10000
	store i32 %_18, i32* %x
	br label %if31
if31:
	br label %if28
if28:
	%_19 = load i32, i32* %i
	%_20 = add i32 %_19, 1
	store i32 %_20, i32* %i
	br label %while7
while9:
	%_21 = load i1, i1* %b
	br label %andcond32
andcond32:
	br i1 %_21, label %andcond33, label %andcond35
andcond33:
	%_22 = load i1, i1* %c
	br label %andcond34
andcond34:
	br label %andcond35
andcond35:
	%_23 = phi i1 [0, %andcond32], [%_22, %andcond34]
	br i1 %_23, label %if36, label %if37
if36:
	store i1 0, i1* %c
	br label %if38
if37:
	%_24 = load i1, i1* %a
	br label %andcond39
andcond39:
	br i1 %_24, label %andcond40, label %andcond42
andcond40:
	%_25 = load i1, i1* %b
	br label %andcond41
andcond41:
	br label %andcond42
andcond42:
	%_26 = phi i1 [0, %andcond39], [%_25, %andcond41]
	br i1 %_26, label %if43, label %if44
if43:
	store i1 0, i1* %b
	br label %if45
if44:
	store i1 0, i1* %a
	br label %if45
if45:
	br label %if38
if38:
	br label %while0
while2:
	%_27 = load i32, i32* %x
	call void (i32) @print_int(i32 %_27)
	ret i32 0
}

