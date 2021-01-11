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
	%b = alloca i1	
	%c = alloca i1	
	%x = alloca i32	
	store i1 0, i1* %b	
	store i1 1, i1* %c	
	%_0 = load i1, i1* %b
	br label %andcond0
andcond0:
    ; Check result, short circuit if false
	br i1 %_0, label %andcond1, label %andcond3
andcond1:
	%_1 = load i1, i1* %c
	br label %andcond2
andcond2:
	; this label seems redundant here and we could have used %andcond1 instead - 
	; but this becomes useful when compiling expressions a && b && c (hint!)
	br label %andcond3
andcond3:
    ; Get appropriate value, depending on the predecessor block
	%_2 = phi i1 [0, %andcond0], [%_1, %andcond2]

	br i1 %_2, label %if4, label %if5
if4:
	store i32 0, i32* %x	
	br label %if6
if5:
	store i32 1, i32* %x	
	br label %if6
if6:
	%_3 = load i32, i32* %x
	call void (i32) @print_int(i32 %_3)
	ret i32 0	
}

