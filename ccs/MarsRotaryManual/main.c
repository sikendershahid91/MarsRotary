/**
 * main.c
 */
#include <stdio.h>
#include <stdint.h>
#include <stdbool.h>
#include "inc/hw_gpio.h"
#include "inc/hw_ints.h"
#include "inc/hw_memmap.h"
#include "inc/hw_types.h"
#include "driverlib/gpio.h"
#include "driverlib/interrupt.h"
#include "driverlib/pin_map.h"
#include "driverlib/sysctl.h"
#include "driverlib/uart.h"
#include "utils/uartstdio.h"
#include "string.h"
#include "sensorlib/i2cm_drv.h"
#include "sensorlib/hw_mpu6050.h"
#include "sensorlib/mpu6050.h"
#include "Reorientation.h"

///////////////////////////////////////
#define MPU6050_I2C_ADDRESS 0x68
tI2CMInstance g_sI2CInst;
tMPU6050 g_sMPU6050Inst;
volatile unsigned long g_vui8DataFlag;
volatile unsigned long g_vui8ErrorFlag;


void MPU6050_APP_CALL_BACK(void *pvCallBackData, uint_fast8_t ui8Status){
    if(ui8Status == I2CM_STATUS_SUCCESS){
        g_vui8DataFlag = 1;
    }
    g_vui8ErrorFlag = ui8Status;
}

void MPU6050_I2C_INTERRUPT_HANDLER(void){
    I2CMIntHandler(&g_sI2CInst);
}

void MPU6050_APP_I2C_WAIT(char *pcFilename, uint_fast32_t ui32Line){
    while((g_vui8DataFlag == 0) && (g_vui8ErrorFlag ==0)){}
    if(g_vui8ErrorFlag){
        UARTprintf("Error in I2C data");
    }
    g_vui8ErrorFlag = 0;
}
///////////////////////////////////////
void I2C_RECEIVE(){

}
void I2C_TRANSMIT(){

}
void I2C_INIT(){

}
void UART_INTERRUPT_HANDLER(void){
    char char_buffer[16] = {0};
    uint32_t ui32_status;
    ui32_status = UARTIntStatus(UART0_BASE,true);
    UARTIntClear(UART0_BASE, ui32_status);
    UARTgets(char_buffer, 17);
    UARTprintf(" result: %s  type a message :", char_buffer );
}

void UART_INIT(){
    // UART Module 0
    // Port PA0 RX
    // Port PA1 TX

    SysCtlPeripheralEnable(SYSCTL_PERIPH_UART0);
    SysCtlPeripheralEnable(SYSCTL_PERIPH_GPIOA);
    // configuration of GPIO pins and types
    GPIOPinConfigure(GPIO_PA0_U0RX);
    GPIOPinConfigure(GPIO_PA1_U0TX);
    GPIOPinTypeUART(GPIO_PORTA_BASE, GPIO_PIN_0 | GPIO_PIN_1);
    // set clock and standard I/O
    UARTClockSourceSet(UART0_BASE, UART_CLOCK_PIOSC);
    UARTStdioConfig(0, 115200, 16000000);
    // Interrupt Enabled for Data on TX and RX
    IntEnable(INT_UART0);
    UARTIntEnable(UART0_BASE, UART_INT_RX | UART_INT_RT);
}

int main(void) {
    uint_fast16_t f_gyroX;
    uint_fast16_t  f_gyroY;
    uint_fast16_t  f_gyroZ;

    uint8_t ui8Mask;
    uint_fast8_t ui8_Transceiver = 0xFF;
    uint_fast8_t ui8_Receiver = 0xFF;
    SysCtlClockSet(SYSCTL_SYSDIV_4 | SYSCTL_USE_PLL | SYSCTL_OSC_MAIN | SYSCTL_XTAL_16MHZ);
    UART_INIT();

    SysCtlPeripheralEnable(SYSCTL_PERIPH_GPIOD);
    SysCtlPeripheralEnable(SYSCTL_PERIPH_I2C3);
    GPIOPinConfigure(GPIO_PD0_I2C3SCL);
    GPIOPinConfigure(GPIO_PD1_I2C3SDA);
    GPIOPinTypeI2CSCL(GPIO_PORTD_BASE, GPIO_PIN_0);
    GPIOPinTypeI2C(GPIO_PORTD_BASE, GPIO_PIN_1);
    IntMasterEnable();

    I2CMInit(&g_sI2CInst, I2C3_BASE, INT_I2C3, ui8_Transceiver, ui8_Receiver, SysCtlClockGet());
    SysCtlDelay(SysCtlClockGet() / 3);
    MPU6050Init(&g_sMPU6050Inst, &g_sI2CInst, MPU6050_I2C_ADDRESS,MPU6050_APP_CALL_BACK,NULL);
    MPU6050_APP_I2C_WAIT(__FILE__, __LINE__);
    ui8Mask = 0;
    MPU6050ReadModifyWrite(&g_sMPU6050Inst, MPU6050_O_PWR_MGMT_1, 0, 1,MPU6050_APP_CALL_BACK,NULL);
    MPU6050_APP_I2C_WAIT(__FILE__, __LINE__);

    while(1){
        MPU6050DataRead(&g_sMPU6050Inst, MPU6050_APP_CALL_BACK, NULL);
        MPU6050_APP_I2C_WAIT(__FILE__, __LINE__);
        MPU6050DataGyroGetRaw(&g_sMPU6050Inst, &f_gyroX,
                                                 &f_gyroY,
                                                 &f_gyroZ);
        UARTprintf("x value %u , y value %u, z value %u \n", f_gyroX, f_gyroY, f_gyroZ);
        UARTprintf(" transceiver-> %u\n", ui8_Transceiver);
        UARTprintf(" receiver----> %u\n", ui8_Receiver);

    }

    return 0;
}
