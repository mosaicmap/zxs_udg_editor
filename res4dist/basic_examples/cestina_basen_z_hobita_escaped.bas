5 REM ---- pokus cestina 3 UDG
20 PAPER 7: BORDER 7: INK 0: BRIGHT 0: FLASH 0: INVERSE 0
30 CLS
40 PRINT "Moment..."
50 GO SUB 9000
60 CLS
201 PRINT AT 1,2; "Rozbij tal{I}{R}, t{R}{I}skni fla{S}{I}!";
202 PRINT AT 2,2; "Zl{A}mej rukojeti l{B}ic!";
203 PRINT AT 3,2; "To pan Pytl{I}k t{Q}{B}ko sn{A}{S}{I} --";
204 PRINT AT 4,2; "sklenkou k{R}{A}pni je{S}t{Q} v{I}c!";
206 PRINT AT 6,2; "Propal ubrus, jenom sm{Q}le!";
207 PRINT AT 7,2; "Vylej ml{I}ko ve sp{I}{B}i!";
208 PRINT AT 8,2; "Kosti vysyp u postele!";
209 PRINT AT 9,2; "V{I}no c{A}kni do d{I}{B}{I}!";
211 PRINT AT 11,2; "{K}{A}lky h{A}zej do mo{B}d{I}{R}e,";
212 PRINT AT 12,2; "pak je roztlu{C} palic{I}!";
213 PRINT AT 13,2; "Kdy{B} nesta{C}{I}{S} na tal{I}{R}e,";
214 PRINT AT 14,2; "koulej s nimi sv{Q}tnic{I}!";
216 PRINT AT 16,2; "To pan Pytl{I}k t{Q}{B}ko sn{A}{S}{I}!";
217 PRINT AT 17,2; "Proto opatrn{Q}, bra{S}i!";
219 PRINT AT 19,7; "-- J.R.R.T v knize Hobit";
8998 PAUSE 0
8999 STOP
9000 REM ---- definice UDG, ukladani da pameti
9001 RESTORE 9010: READ pocet
9002 FOR i=1 TO pocet: READ C$
9003 FOR j=0 TO 7: READ B: POKE USR C$+j,B
9004 NEXT j: NEXT i
9005 RETURN
9010 DATA 13: REM .. 13 zakladnich pismen s diakritikou
9011 DATA "A",8,16,56,4,60,68,60,0: REM a'
9012 DATA "B",40,16,124,8,16,32,124,0: REM z''
9013 DATA "C",20,8,28,32,32,32,28,0: REM c''
9014 DATA "E",8,16,56,68,120,64,60,0: REM e'
9015 DATA "H",8,16,68,68,68,60,4,56: REM y'
9016 DATA "I",8,16,0,48,16,16,56,0: REM i'
9017 DATA "J",8,16,68,68,68,68,56,0: REM u'
9018 DATA "K",36,60,64,60,2,66,60,0: REM S''
9019 DATA "N",40,16,120,68,68,68,68,0: REM n'' (-)
9020 DATA "O",8,16,56,68,68,68,56,0: REM o' (-)
9021 DATA "Q",40,16,56,68,120,64,60,0: REM e''
9022 DATA "R",20,8,28,32,32,32,32,0: REM r''
9023 DATA "S",40,16,56,64,56,4,120,0: REM s''
9024 DATA "U",16,40,16,68,68,68,56,0: REM u'''
