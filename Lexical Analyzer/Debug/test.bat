@ECHO OFF

CALL Assignment3.exe a3r.pls > a3r.seva
CALL Assignment3.exe a3w1.pls > a3w1.seva
CALL Assignment3.exe a3w2.pls > a3w2.seva
CALL Assignment3.exe a3w3.pls > a3w3.seva
CALL Assignment3.exe a3w4.pls > a3w4.seva

fc /b a3r.sout a3r.seva
fc /b a3w1.sout a3w1.seva
fc /b a3w2.sout a3w2.seva
fc /b a3w3.sout a3w3.seva
fc /b a3w4.sout a3w4.seva