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

#include "Reorientation.h"

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
    int a = foo(4);
    SysCtlClockSet(SYSCTL_SYSDIV_4 | SYSCTL_USE_PLL | SYSCTL_OSC_MAIN | SYSCTL_XTAL_16MHZ);
    UART_INIT();
    return 0;
}
