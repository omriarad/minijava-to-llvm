@.Fac_vtable = global [1 x i8*] [i8* bitcast (i32 (i8*, i32)* @Fac.ComputeFac to i8*)]

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
	%_2 = getelementptr [1 x i8*], [1 x i8*]* @.Fac_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	%_3 = bitcast i8* %_0 to i8***
	%_4 = load i8**, i8*** %_3
	%_5 = getelementptr i8*, i8** %_4, i32 0
	%_6 = load i8*, i8** %_5
	%_7 = bitcast i8* %_6 to i32 (i8*, i32)*
	%_8 = call i32 %_7(i8* %_0, i32 10)
	call void (i32) @print_int(i32 %_8)
	ret i32 0
}

define i32 @Fac.ComputeFac(i8* %this, i32 %.num) {
	%num = alloca i32	
	store i32 %.num, i32* %num	
	%num_aux = alloca i32	
	%_0 = load i32, i32* %num
	%_1 = icmp slt i32 %_0, 1
	br i1 %_1, label %if0, label %if1
if0:
	store i32 1, i32* %num_aux	
	
	br label %if2
if1:
	%_2 = load i32, i32* %num
	%_3 = bitcast i8* %this to i8***
	%_4 = load i8**, i8*** %_3
	%_5 = getelementptr i8*, i8** %_4, i32 0
	%_6 = load i8*, i8** %_5
	%_7 = bitcast i8* %_6 to i32 (i8*, i32)*
	%_8 = load i32, i32* %num
	%_9 = sub i32 %_8, 1
	%_10 = call i32 %_7(i8* %this, i32 %_9)
	%_11 = mul i32 %_2, %_10
	store i32 %_11, i32* %num_aux	
	
	br label %if2
if2:
	%_12 = load i32, i32* %num_aux
	ret i32 %_12	
}

