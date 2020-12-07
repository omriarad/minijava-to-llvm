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
	br label %andcond12
andcond12:
	br label %andcond13
andcond13:
	%_6 = phi i1 [0, %andcond10], [%_5, %andcond12]
	br label %andcond14
andcond14:
	br i1 %_6, label %andcond15, label %andcond17
andcond15:
	%_7 = load i1, i1* %c
	br label %andcond18
andcond18:
	br i1 %_7, label %andcond19, label %andcond21
andcond19:
	%_8 = load i1, i1* %c
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
	br i1 %_10, label %if22, label %if23
if22:
	%_11 = load i32, i32* %x
	%_12 = add i32 %_11, 1
	store i32 %_12, i32* %x
	br label %if24
if23:
	%_13 = load i1, i1* %b
	br i1 %_13, label %if25, label %if26
if25:
	%_14 = load i32, i32* %x
	%_15 = add i32 %_14, 100
	store i32 %_15, i32* %x
	br label %if27
if26:
	%_16 = load i32, i32* %x
	%_17 = add i32 %_16, 10000
	store i32 %_17, i32* %x
	br label %if27
if27:
	br label %if24
if24:
	%_18 = load i32, i32* %i
	%_19 = add i32 %_18, 1
	store i32 %_19, i32* %i
	br label %while7
while9:
	%_20 = load i1, i1* %a
	br label %andcond28
andcond28:
	br i1 %_20, label %andcond29, label %andcond31
andcond29:
	%_21 = load i1, i1* %b
	br label %andcond30
andcond30:
	br label %andcond31
andcond31:
	%_22 = phi i1 [0, %andcond28], [%_21, %andcond30]
	br i1 %_22, label %if32, label %if33
if32:
	store i1 0, i1* %a
	br label %if34
if33:
	%_23 = load i1, i1* %b
	br label %andcond35
andcond35:
	br i1 %_23, label %andcond36, label %andcond38
andcond36:
	%_24 = load i1, i1* %c
	br label %andcond37
andcond37:
	br label %andcond38
andcond38:
	%_25 = phi i1 [0, %andcond35], [%_24, %andcond37]
	br i1 %_25, label %if39, label %if40
if39:
	store i1 0, i1* %b
	br label %if41
if40:
	store i1 0, i1* %c
	br label %if41
if41:
	br label %if34
if34:
	br label %while0
while2:
	%_26 = load i32, i32* %x
	call void (i32) @print_int(i32 %_26)
	ret i32 0
}

