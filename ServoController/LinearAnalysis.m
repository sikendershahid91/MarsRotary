clear 
clc
close all

%% Reference the simulink model for the motor
model = 'ServoBD';
%% Get the analysis I/Os from the model
io = getlinio(model);
%% Specify the operating point
op = operpoint(model);

%% Linearize the model
sys = linearize(model,io,op);
%% extract transfer function and state space matrix
TransferFunction=tf(sys)
StateSpace=sys
%% stability?
poles=pole(TransferFunction)
zeros=zero(TransferFunction)
eigenvalues=eig(StateSpace)
%% step response + impulse response, map poles/zeros, root locus map
figure(1)
step(sys)
figure(2)
impulse(sys)
figure(4)
pzmap(sys)
figure(5)
rlocus(sys)

%% Controllability Matrix
P = [sys.b,sys.a*sys.b,(sys.a)^2*sys.b];
% Check for controllability
Pinv = inv(P); 
rankA = rank(sys.a);
rankP = rank(P);
% Transformation Matrix
Transform1 = [0 0 1]*Pinv;
TransformMat = [Transform1;
    Transform1*sys.a;
    Transform1*(sys.a)^2;
    ];
% Controller Type Companion Form
Ac = TransformMat*sys.a*inv(TransformMat);
Bc = TransformMat*sys.b;
Cc = sys.c*inv(TransformMat);
sys_controllerType = ss(Ac,Bc,Cc,0)
% Verify the system tranformation (tf should be the same as original)
tFuncController = tf(sys_controllerType)


%% Controller Design: Place Eigenvalues (randomnly) at -3,-2+j,-2-j
% Desired characteristic equation 
desPoly = poly([-3,-2+1i,-2-1i]);
% Controller-type state feedback vector
kc3 = desPoly(4)+Ac(3,1);
kc2 = desPoly(3)+Ac(3,2);
kc1 = desPoly(2)+Ac(3,3);
Kc = [kc3 kc2 kc1];
% New Controller type system matrix
Ac_hat = Ac-Bc*Kc;
new_eigenvalues = eig(Ac_hat);
% General state feedback vector
K = Kc*TransformMat;
% Modified stable system (in original format)
A_new = sys.a - (sys.b * K);
sys_new = ss(A_new,sys.b,sys.c,0);
%% Plot for new response
figure(6)
impulse(sys_new)
figure(7)
step(sys_new)
figure(8)
bode(sys_new)



