@.Classes_vtable = global [1 x i8*] [i8* bitcast (i32 (i8*)* @Classes.run to i8*)]

@.Base_vtable = global [2 x i8*] [i8* bitcast (i32 (i8*, i32)* @Base.set to i8*), i8* bitcast (i32 (i8*)* @Base.get to i8*)]

@.Derived_vtable = global [3 x i8*] [i8* bitcast (i32 (i8*, i32)* @Derived.set to i8*), i8* bitcast (i32 (i8*)* @Base.get to i8*), i8* bitcast (i32 (i8*)* @Derived.poop to i8*)]

@.DerivedFromDerived_vtable = global [3 x i8*] [i8* bitcast (i32 (i8*, i32)* @Derived.set to i8*), i8* bitcast (i32 (i8*)* @DerivedFromDerived.get to i8*), i8* bitcast (i32 (i8*)* @Derived.poop to i8*)]

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
	%_2 = getelementptr [1 x i8*], [1 x i8*]* @.Classes_vtable, i32 0, i32 0
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

define i32 @Classes.run(i8* %this) {
	%b = alloca i8*
	%d = alloca i8*
	%e = alloca i8*
	%_0 = call i8* @calloc(i32 1, i32 16)
	%_1 = bitcast i8* %_0 to i8***
	%_2 = getelementptr [3 x i8*], [3 x i8*]* @.DerivedFromDerived_vtable, i32 0, i32 0
	store i8** %_2, i8*** %_1
	store i8* %_0, i8** %e
	%_3 = call i8* @calloc(i32 1, i32 12)
	%_4 = bitcast i8* %_3 to i8***
	%_5 = getelementptr [2 x i8*], [2 x i8*]* @.Base_vtable, i32 0, i32 0
	store i8** %_5, i8*** %_4
	store i8* %_3, i8** %b
	%_6 = call i8* @calloc(i32 1, i32 12)
	%_7 = bitcast i8* %_6 to i8***
	%_8 = getelementptr [3 x i8*], [3 x i8*]* @.Derived_vtable, i32 0, i32 0
	store i8** %_8, i8*** %_7
	store i8* %_6, i8** %d
	%_9 = load i8*, i8** %e
	%_10 = bitcast i8* %_9 to i8***
	%_11 = load i8**, i8*** %_10
	%_12 = getelementptr i8*, i8** %_11, i32 2
	%_13 = load i8*, i8** %_12
	%_14 = bitcast i8* %_13 to i32 (i8*)*
	%_15 = call i32 %_14(i8* %_9)
	call void (i32) @print_int(i32 %_15)
	%_16 = load i8*, i8** %d
	%_17 = bitcast i8* %_16 to i8***
	%_18 = load i8**, i8*** %_17
	%_19 = getelementptr i8*, i8** %_18, i32 2
	%_20 = load i8*, i8** %_19
	%_21 = bitcast i8* %_20 to i32 (i8*)*
	%_22 = call i32 %_21(i8* %_16)
	call void (i32) @print_int(i32 %_22)
	%_23 = load i8*, i8** %e
	%_24 = bitcast i8* %_23 to i8***
	%_25 = load i8**, i8*** %_24
	%_26 = getelementptr i8*, i8** %_25, i32 0
	%_27 = load i8*, i8** %_26
	%_28 = bitcast i8* %_27 to i32 (i8*, i32)*
	%_29 = call i32 %_28(i8* %_23, i32 1)
	call void (i32) @print_int(i32 %_29)
	%_30 = load i8*, i8** %b
	%_31 = bitcast i8* %_30 to i8***
	%_32 = load i8**, i8*** %_31
	%_33 = getelementptr i8*, i8** %_32, i32 0
	%_34 = load i8*, i8** %_33
	%_35 = bitcast i8* %_34 to i32 (i8*, i32)*
	%_36 = call i32 %_35(i8* %_30, i32 1)
	call void (i32) @print_int(i32 %_36)
	%_37 = load i8*, i8** %b
	%_38 = bitcast i8* %_37 to i8***
	%_39 = load i8**, i8*** %_38
	%_40 = getelementptr i8*, i8** %_39, i32 1
	%_41 = load i8*, i8** %_40
	%_42 = bitcast i8* %_41 to i32 (i8*)*
	%_43 = call i32 %_42(i8* %_37)
	call void (i32) @print_int(i32 %_43)
	%_44 = load i8*, i8** %d
	store i8* %_44, i8** %b
	%_45 = load i8*, i8** %b
	%_46 = bitcast i8* %_45 to i8***
	%_47 = load i8**, i8*** %_46
	%_48 = getelementptr i8*, i8** %_47, i32 1
	%_49 = load i8*, i8** %_48
	%_50 = bitcast i8* %_49 to i32 (i8*)*
	%_51 = call i32 %_50(i8* %_45)
	call void (i32) @print_int(i32 %_51)
	%_52 = load i8*, i8** %b
	%_53 = bitcast i8* %_52 to i8***
	%_54 = load i8**, i8*** %_53
	%_55 = getelementptr i8*, i8** %_54, i32 0
	%_56 = load i8*, i8** %_55
	%_57 = bitcast i8* %_56 to i32 (i8*, i32)*
	%_58 = call i32 %_57(i8* %_52, i32 3)
	call void (i32) @print_int(i32 %_58)
	%_59 = load i8*, i8** %b
	%_60 = bitcast i8* %_59 to i8***
	%_61 = load i8**, i8*** %_60
	%_62 = getelementptr i8*, i8** %_61, i32 1
	%_63 = load i8*, i8** %_62
	%_64 = bitcast i8* %_63 to i32 (i8*)*
	%_65 = call i32 %_64(i8* %_59)
	call void (i32) @print_int(i32 %_65)
	%_66 = load i8*, i8** %d
	%_67 = bitcast i8* %_66 to i8***
	%_68 = load i8**, i8*** %_67
	%_69 = getelementptr i8*, i8** %_68, i32 1
	%_70 = load i8*, i8** %_69
	%_71 = bitcast i8* %_70 to i32 (i8*)*
	%_72 = call i32 %_71(i8* %_66)
	call void (i32) @print_int(i32 %_72)
	%_73 = load i8*, i8** %e
	store i8* %_73, i8** %b
	%_74 = load i8*, i8** %b
	%_75 = bitcast i8* %_74 to i8***
	%_76 = load i8**, i8*** %_75
	%_77 = getelementptr i8*, i8** %_76, i32 1
	%_78 = load i8*, i8** %_77
	%_79 = bitcast i8* %_78 to i32 (i8*)*
	%_80 = call i32 %_79(i8* %_74)
	call void (i32) @print_int(i32 %_80)
	%_81 = load i8*, i8** %b
	%_82 = bitcast i8* %_81 to i8***
	%_83 = load i8**, i8*** %_82
	%_84 = getelementptr i8*, i8** %_83, i32 0
	%_85 = load i8*, i8** %_84
	%_86 = bitcast i8* %_85 to i32 (i8*, i32)*
	%_87 = call i32 %_86(i8* %_81, i32 3)
	call void (i32) @print_int(i32 %_87)
	%_88 = load i8*, i8** %b
	%_89 = bitcast i8* %_88 to i8***
	%_90 = load i8**, i8*** %_89
	%_91 = getelementptr i8*, i8** %_90, i32 1
	%_92 = load i8*, i8** %_91
	%_93 = bitcast i8* %_92 to i32 (i8*)*
	%_94 = call i32 %_93(i8* %_88)
	call void (i32) @print_int(i32 %_94)
	%_95 = load i8*, i8** %d
	%_96 = bitcast i8* %_95 to i8***
	%_97 = load i8**, i8*** %_96
	%_98 = getelementptr i8*, i8** %_97, i32 1
	%_99 = load i8*, i8** %_98
	%_100 = bitcast i8* %_99 to i32 (i8*)*
	%_101 = call i32 %_100(i8* %_95)
	call void (i32) @print_int(i32 %_101)
	ret i32 0
}

define i32 @Base.set(i8* %this, i32 %.x) {
	%x = alloca i32
	store i32 %.x, i32* %x
	%_0 = load i32, i32* %x
	%_1 = getelementptr i8, i8* %this, i32 8
	%_2 = bitcast i8* %_1 to i32*
	store i32 %_0, i32* %_2
	%_3 = getelementptr i8, i8* %this, i32 8
	%_4 = bitcast i8* %_3 to i32*
	%_5 = load i32, i32* %_4
	ret i32 %_5
}

define i32 @Base.get(i8* %this) {
	%_0 = getelementptr i8, i8* %this, i32 8
	%_1 = bitcast i8* %_0 to i32*
	%_2 = load i32, i32* %_1
	ret i32 %_2
}

define i32 @Derived.poop(i8* %this) {
	call void (i32) @print_int(i32 7)
	%_0 = getelementptr i8, i8* %this, i32 8
	%_1 = bitcast i8* %_0 to i32*
	%_2 = load i32, i32* %_1
	ret i32 %_2
}

define i32 @Derived.set(i8* %this, i32 %.x) {
	%x = alloca i32
	store i32 %.x, i32* %x
	%_0 = load i32, i32* %x
	%_1 = mul i32 %_0, 2
	%_2 = getelementptr i8, i8* %this, i32 8
	%_3 = bitcast i8* %_2 to i32*
	store i32 %_1, i32* %_3
	%_4 = getelementptr i8, i8* %this, i32 8
	%_5 = bitcast i8* %_4 to i32*
	%_6 = load i32, i32* %_5
	ret i32 %_6
}

define i32 @DerivedFromDerived.get(i8* %this) {
	%_0 = getelementptr i8, i8* %this, i32 8
	%_1 = bitcast i8* %_0 to i32*
	%_2 = load i32, i32* %_1
	%_3 = mul i32 %_2, 4
	%_4 = getelementptr i8, i8* %this, i32 8
	%_5 = bitcast i8* %_4 to i32*
	store i32 %_3, i32* %_5
	%_6 = getelementptr i8, i8* %this, i32 12
	%_7 = bitcast i8* %_6 to i32*
	store i32 1, i32* %_7
	%_8 = getelementptr i8, i8* %this, i32 12
	%_9 = bitcast i8* %_8 to i32*
	%_10 = load i32, i32* %_9
	%_11 = getelementptr i8, i8* %this, i32 8
	%_12 = bitcast i8* %_11 to i32*
	%_13 = load i32, i32* %_12
	%_14 = add i32 %_10, %_13
	%_15 = getelementptr i8, i8* %this, i32 12
	%_16 = bitcast i8* %_15 to i32*
	store i32 %_14, i32* %_16
	%_17 = getelementptr i8, i8* %this, i32 12
	%_18 = bitcast i8* %_17 to i32*
	%_19 = load i32, i32* %_18
	ret i32 %_19
}

