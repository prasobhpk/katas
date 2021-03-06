#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <assert.h>
#include <string.h>


#define with_args(values...) values
#define format_as(resultVar, format, formatArgs) char *resultVar = (char *) malloc(10000); sprintf(resultVar, format, formatArgs);

void stringShouldEqual(const char expected[], const char actual[]) {
	int result = strcmp(expected, actual);
	if (result == 0) return;

	fprintf(stderr, "Expected:\n'%s'\nbut was\n'%s'\n", expected, actual);
	assert(0);
}

void intsShouldEqual(int expected, int actual) {
	if (expected == actual) return;
	fprintf(stderr, "Expected: '%d' but was '%d'\n", expected, actual);
	assert(0);
}

int my_power(int base, int n) {
	int i, p;
	p = 1;
	for (i = 1; i <= n; ++i) p = p * base;
	return p;
}

void part_1_1() {
	printf("hello, !!!! ooooa aaa ");
	printf("world");
	printf("\n");
}

void part_1_2() {
	#define LOWER  0
    #define UPPER  300
    #define STEP   20

    int fahr = LOWER;
    while (fahr <= UPPER) {
        int celsius = 5 * (fahr-32) / 9;
        printf("%d\t%d\n", fahr, celsius);
        fahr = fahr + STEP;
    }

    for (fahr = LOWER; fahr <= UPPER; fahr = fahr + STEP)
        printf("%3d %6.1f\n", fahr, (5.0/9.0)*(fahr-32));
}


char *fakeInput = "asdf ghjkl!!\nanother line\nohh!!";
int fakeInputSize = 0;
int fakeInputPosition = 0;
// fake function to avoid real input
char getchar_fake() {
	if (fakeInputPosition == strlen(fakeInput)) return EOF;
	else return fakeInput[fakeInputPosition++];
}

void init_fake_input_to(char s[]) {
	fakeInput = s;
	fakeInputSize = strlen(s);
	fakeInputPosition = 0;
}


void part_1_5_1() {
	fakeInputPosition = 0;

    char c;
    while ((c = getchar_fake()) != EOF) putchar(c);
}

void part_1_5_2() {
	init_fake_input_to("asdf ghjkl!!\nanother line\nohh!!");

    long nc = 0;
//    while (getchar_fake() != EOF) ++nc;
    for (nc = 0; getchar_fake() != EOF; ++nc);

    format_as(actual, "%ld\n", nc);
    stringShouldEqual("31\n", actual);
}

void part_1_5_3() {
	fakeInputPosition = 0;

    int c, nl;
    nl = 0;
    while ((c = getchar_fake()) != EOF) {
        if (c == '\n') ++nl;
    }

    format_as(actual, "%d\n", nl);
    stringShouldEqual("2\n", actual);
}

void part_1_5_4() {
	fakeInputPosition = 0;

    #define IN   1  /* inside a word */
    #define OUT  0  /* outside a word */

	int c, nl, nw, nc, state;
    state = OUT;
    nl = nw = nc = 0;
    while ((c = getchar_fake()) != EOF) {
        ++nc;
        if (c == '\n')
            ++nl;
        if (c == ' ' || c == '\n' || c == '\t')
            state = OUT;
        else if (state == OUT) {
            state = IN;
			++nw;
		}
	}

	format_as(actual, "%d %d %d\n", with_args(nl, nw, nc));
	stringShouldEqual("2 5 31\n", actual);
}

void part_1_6() {
    int ndigit[10];
    ndigit[0] = 12;
    ndigit[1] = 14;

    int i;
    for (i = 0; i < 10; ++i) {
        printf(" %d", ndigit[i]);
    }
}

void part_1_7() {
	int i;
	for (i = 0; i < 10; ++i)
	    printf("%d %d %d\n", i, my_power(2,i), my_power(-3,i));
}

/* getline:  read a line into s, return length  */
int getline_1_8(char s[], int lim) {
	int c, i;
	for (i=0; i < lim-1 && (c=getchar_fake())!=EOF && c!='\n'; ++i)
		s[i] = c;
	if (c == '\n') {
        s[i] = c;
		++i;
	}
	s[i] = '\0';
	return i;
}

/* copy:  copy 'from' into 'to'; assume to is big enough */
void copy(char to[], char from[]) {
	int i;
    i = 0;
    while ((to[i] = from[i]) != '\0') ++i;
}

void part_1_8() {
	#define MAXLINE 1000   /* maximum input line length */

	fakeInputPosition = 0;

	int len; /* longest line saved here */
	int max; /* current line length */
	char line[MAXLINE]; /* maximum length seen so far */
	char longest[MAXLINE]; /* current input line */

	max = 0;
	while ((len = getline_1_8(line, MAXLINE)) > 0)
	   if (len > max) {
	       max = len;
	       copy(longest, line);
	   }
	if (max > 0) /* there was a line */
	   printf("%s", longest);
}

void part_2_2() {
    short int shortInt;
    long int longInt;
    // short float short Float;  // error
    // long float longFloat;     // error
    // short double shortDouble; // error
    long double longDouble;

    signed char signedChar;
    unsigned char unsignedChar;
    signed int signedInt;
    unsigned int unsignedInt;
    // signed double signedDouble; // error
    // unsigned double unsignedDouble; // error
}

void part_2_3() {
    #define A_LONG 12345L
    #define AN_UNSIGNED_LONG 12345UL

    enum months { JAN = 1, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC };
    printf("%d, %d, %d", JAN, FEB, MAR);
}

int my_atoi(char s[]) {
	int i, n;
	n = 0;
	for (i = 0; s[i] >= '0' && s[i] <= '9'; ++i)
	   n = 10 * n + (s[i] - '0');
	return n;
}

int my_lower(int c) {
	if (c >= 'A' && c <= 'Z')
	   return c + 'a' - 'A';
	else
	   return c;
}

void part_2_7() {
	printf("%d\n", my_atoi("12345"));
	printf("%d\n", my_atoi("12345qwerty"));
	printf("%c", my_lower('P'));
	printf("%c", my_lower('I'));
	printf("%c", my_lower('g'));
}

#define MAXOP   100  /* max size of operand or operator */
#define NUMBER  '0'  /* signal that a number was found */

#define MAXVAL 100  /* maximum depth of val stack */
int sp = 0;         /* next free stack position */
double val[MAXVAL]; /* value stack */

/* push:  push f onto value stack */
void push(double f) {
	if (sp < MAXVAL)
		val[sp++] = f;
	else
		printf("error: stack full, can�t push %g\n", f);
}

/* pop:  pop and return top value from stack */
double pop(void) {
	if (sp > 0) {
		return val[--sp];
	} else {
		printf("error: stack empty\n");
		return 0.0;
	}
}

#define BUFSIZE 100
char buf[BUFSIZE];    /* buffer for ungetch */
int bufp = 0;         /* next free position in buf */

/* get a (possibly pushed-back) character */
int getch(void) {
	return (bufp > 0) ? buf[--bufp] : getchar_fake();
}

/* push character back on input */
void ungetch(int c) {
	if (bufp >= BUFSIZE)
		printf("ungetch: too many characters\n");
	else
		buf[bufp++] = c;
}

int getop(char s[]) {
	int i, c;
	while ((s[0] = c = getch()) == ' ' || c == '\t');

	s[1] = '\0';
	if (!isdigit(c) && c != '.') return c; /* not a number */

	i = 0;
	if (isdigit(c)) /* collect integer part */
		while (isdigit(s[++i] = c = getch()));

	if (c == '.') /* collect fraction part */
		while (isdigit(s[++i] = c = getch()));

	s[i] = '\0';
	if (c != EOF) ungetch(c);
	return NUMBER;
}

void part_4_3() {
    init_fake_input_to("2 2 3 + *\n");

	int type;
	double op2;
	char s[MAXOP];

	while ((type = getop(s)) != EOF) {
		printf("%s\n", s);

		switch (type) {
			case NUMBER:
				push(atof(s));
				break;
			case '+':
				push(pop() + pop());
				break;
			case '*':
				push(pop() * pop());
				break;
			case '-':
				op2 = pop();
				push(pop() - op2);
				break;
			case '/':
				op2 = pop();
				if (op2 != 0.0)
				   push(pop() / op2);
				else
				   printf("error: zero divisor\n");
				break;
			case '\n':
				printf("\t%.8g\n", pop());
				break;
			default:
				printf("error: unknown command %s\n", s);
			break;
		}
	}
}

void increaseCounter() {
	static int internalStaticCounter;
	printf("%d\n", internalStaticCounter++);
}

void part_4_6() {
	increaseCounter();
	increaseCounter();
	increaseCounter();
}

void printDecimal(int n) {
	if (n < 0) {
		putchar('-');
		n = -n;
	}
	if (n / 10) printDecimal(n / 10);
	putchar(n % 10 + '0');
}

void my_swap(int v[], int i, int j) {
	int temp = v[i];
	v[i] = v[j];
	v[j] = temp;
}

void my_qsort(int v[], int left, int right) {
	int i, last;

	if (left >= right) return;

	my_swap(v, left, (left + right) / 2); /* move partition elem */
	last = left;                          /* to v[0] */
	for (i = left + 1; i <= right; i++)  /* partition */
		if (v[i] < v[left]) my_swap(v, ++last, i);

	my_swap(v, left, last); /* restore partition  elem */
	my_qsort(v, left, last-1);
	my_qsort(v, last+1, right);
}

void part_4_10() {
	printDecimal(1234);
	printf("\n");

	int values[] = {2, 1, 4, 3};
	my_qsort(values, 0, 4);

	int i;
	for (i = 0; i < 4; i++) {
		printDecimal(values[i]);
		printf(" ");
	}
	printf("\n");
}

// to see preprocessor output: gcc -E <filename>
void part_4_11() {
	#define  dprint(expr)   printf(#expr " = %g\n", expr)
	double x = 12;
	double y = 24;
    dprint(x/y);

    #define paste(front, back) front ## back
    double paste(x, 11) = 123;
    dprint(x11);
}

void part_5_1() {
	int x = 1, y = 2, z[10];
	int *ip; /* ip is a pointer to int */

    ip = &x; /* ip now points to x */
    y = *ip; /* y is now 1 */
    *ip = 0; /* x is now 0 */
    ip = &z[0]; /* ip now points to z[0] */

	*ip = *ip + 10; // increments *ip by 10
    y = *ip + 1; // takes whatever ip points at, adds 1, and assigns the result to y
    *ip += 1; // increments what ip points to, as do
    ++*ip;
    //and
    (*ip)++;
}

void swap_5_2(int *a, int *b) {
	int tmp = *a;
	*a = *b;
	*b = tmp;
}

/* getint:  get next integer from input into *pn */
int getint(int *pn) {
	int c, sign;
	while (isspace(c = getch()));
	if (!isdigit(c) && c != EOF && c != '+' && c != '-') {
		ungetch(c);  /* it is not a number */
		return 0;
	}

	sign = (c == '-') ? -1 : 1;
	if (c == '+' || c == '-') c = getch();

	for (*pn = 0; isdigit(c); c = getch())
		*pn = 10 * *pn + (c - '0');
	*pn *= sign;
	if (c != EOF) ungetch(c);
	return c;
}

void part_5_2() {
	int a = 12;
	int b = 23;
	swap_5_2(&a, &b);
	printf("%d, %d\n", a, b);

	init_fake_input_to("123 234 -345");
	int result;
	getint(&result); printf("%d\n", result);
	getint(&result); printf("%d\n", result);
	getint(&result); printf("%d\n", result);
}

#define ALLOCSIZE 10000 /* size of available space */
static char allocbuf[ALLOCSIZE]; /* storage for alloc */
static char *allocp = allocbuf;  /* next free position */

char *alloc(int n) {    /* return pointer to n characters */
	if (allocbuf + ALLOCSIZE - allocp >= n) {  /* it fits */
		allocp += n;
		return allocp - n; /* old p */
	} else {     /* not enough room */
		return 0;
	}
}
void afree(char *p) {  /* free storage pointed to by p */
	if (p >= allocbuf && p < allocbuf + ALLOCSIZE)
		allocp = p;
}

void part_5_4() {
	char *s1 = alloc(20);
	char *s2 = alloc(10);
	afree(s2);
	afree(s1);
}

/* strcpy:  copy t to s; array subscript version */
void strcpy_1(char *s, char *t) {
	int i;
	i = 0;
	while ((s[i] = t[i]) != '\0') i++;
}
/* strcpy:  copy t to s; pointer version */
void strcpy_2(char *s, char *t) {
	int i;
	i = 0;
	while ((*s = *t) != '\0') {
		s++;
		t++;
	}
}
/* strcpy:  copy t to s; pointer version 2 */
void strcpy_3(char *s, char *t) {
	while ((*s++ = *t++) != '\0');
}
/* strcpy:  copy t to s; pointer version 3 */
void strcpy_4(char *s, char *t) {
	while (*s++ = *t++);
}
/* array version */
int strcmp_1(char *s, char *t) {
	int i;
	for (i = 0; s[i] == t[i]; i++)
		if (s[i] == '\0') return 0;
	return s[i] - t[i];
}
/* pointer version */
int strcmp_2(char *s, char *t) {
	for (; *s == *t; s++, t++)
		if (*s == '\0') return 0;
	return *s - *t;
}

void part_5_5() {
	char abc[] = "abc";
	char xyz[] = "xyz";
	char s[] = "---";

	strcpy_1(s, abc);
	printf("s: %s\n", s);

	strcpy_2(s, xyz);
	printf("s: %s\n", s);

	strcpy_3(s, "cde"); // can also use string literals (!)
	printf("s: %s\n", s);

	strcpy_4(s, "fgh");
	printf("s: %s\n", s);

	int result = 0;
	result = strcmp_1("abc", "abc"); printf("%d\n", result);
	result = strcmp_1("abc", "abd"); printf("%d\n", result);
	result = strcmp_2("abc", "abc"); printf("%d\n", result);
	result = strcmp_2("abc", "abd"); printf("%d\n", result);
}

#define MAXLINES 5000     /* max #lines to be sorted */
char *lineptr[MAXLINES];  /* pointers to text lines */
#define MAXLEN 1000  /* max length of any input line */

/* readlines:  read input lines */
int readlines(char *lineptr[], int maxlines) {
	int len, nlines;
	char *p, line[MAXLEN];
	nlines = 0;
	while ((len = getline_1_8(line, MAXLEN)) > 0)
		if (nlines >= maxlines || (p = alloc(len)) == NULL) {
			return -1;
		} else {
			line[len-1] = '\0';  /* delete newline */
			strcpy(p, line);
			lineptr[nlines++] = p;
		}
	return nlines;
}
/* writelines:  write output lines */
void writelines(char *lineptr[], int nlines) {
	int i;
	for (i = 0; i < nlines; i++)
		printf("%s\n", lineptr[i]);
}
void swap_5_6(char *v[], int i, int j) {
	char *temp;
	temp = v[i];
	v[i] = v[j];
	v[j] = temp;
}
/* qsort:  sort v[left]...v[right] into increasing order */
void qsort_5_6(char *v[], int left, int right) {
	int i, last;

	if (left >= right) return;

	swap_5_6(v, left, (left + right) / 2);
	last = left;
	for (i = left + 1; i <= right; i++)
		if (strcmp(v[i], v[left]) < 0)
			swap_5_6(v, ++last, i);

	swap_5_6(v, left, last);
	qsort_5_6(v, left, last - 1);
	qsort_5_6(v, last + 1, right);
}

void part_5_6() {
	init_fake_input_to("hoho\nunsorted\nlines\naaa\nzzz\n");

	int nlines;     /* number of input lines read */
	if ((nlines = readlines(lineptr, MAXLINES)) >= 0) {
		qsort_5_6(lineptr, 0, nlines-1);
		writelines(lineptr, nlines);
	} else {
		printf("error: input too big to sort\n");
    }
}

static char daytab[2][13] = {
	{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
	{0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}
};
/* day_of_year:  set day of year from month & day */
int day_of_year(int year, int month, int day) {
	int i, leap;
	leap = year%4 == 0 && year%100 != 0 || year%400 == 0;
	for (i = 1; i < month; i++)
		day += daytab[leap][i];
	return day;
}
/* month_day:  set month, day from day of year */
void month_day(int year, int yearday, int *pmonth, int *pday) {
	int i, leap;
	leap = year%4 == 0 && year%100 != 0 || year%400 == 0;
	for (i = 1; yearday > daytab[leap][i]; i++)
		yearday -= daytab[leap][i];
	*pmonth = i;
	*pday = yearday;
}
void part_5_7() {
    int dayOfYear = day_of_year(2012, 2, 27);
    printf("dayOfYear: %d\n", dayOfYear);

    int month;
    int day;
    month_day(2012, 58, &month, &day);
    printf("month: %d, day: %d\n", month, day);

	// f(int (*daytab)[13]); // which says that the parameter is a pointer to an array of 13 integers. Without parentheses, the declaration
    // int *daytab[13]; // is an array of 13 pointers to integers
}

/* month_name:  return name of n-th month */
char *month_name(int n) {
	static char *name[] = {
		"Illegal month",
		"January", "February", "March", "April", "May", "June",
		"July", "August", "September", "October", "November", "December"
	};
	return (n < 1 || n > 12) ? name[0] : name[n];
}
void part_5_8() {
	printf("%s\n", month_name(0));
	printf("%s\n", month_name(1));
	printf("%s\n", month_name(2));
}

/* numcmp:  compare s1 and s2 numerically */
int numcmp(char *s1, char *s2) {
	double v1, v2;
	v1 = atof(s1);
	v2 = atof(s2);

	if (v1 < v2) return -1;
	else if (v1 > v2) return 1;
	else return 0;
}
void swap_5_11(void *v[], int i, int j) {
	void *temp;
	temp = v[i];
	v[i] = v[j];
	v[j] = temp;
}
void qsort_5_11(void *v[], int left, int right, int (*comp)(void *, void *)) {
	int i, last;

	if (left >= right) return;

	swap_5_11(v, left, (left + right) / 2);
	last = left;
	for (i = left + 1; i <= right;  i++)
		if ((*comp)(v[i], v[left]) < 0)
			swap_5_11(v, ++last, i);

	swap_5_11(v, left, last);
	qsort_5_11(v, left, last-1, comp);
	qsort_5_11(v, last+1, right, comp);
}
void part_5_11() {
	init_fake_input_to("hoho\nunsorted\nlines\naaa\nzzz\n");

	int nlines;     /* number of input lines read */
	int numeric = 0;   /* 1 if numeric sort */

	if ((nlines = readlines(lineptr, MAXLINES)) >= 0) {
		qsort_5_11((void**) lineptr, 0, nlines - 1,
                   (int (*)(void*, void*))(numeric ? numcmp : strcmp_1));
		writelines(lineptr, nlines);
	} else {
		printf("error: input too big to sort\n");
    }
}


#define MAXTOKEN  100
enum { NAME, PARENS, BRACKETS };

void dcl(void);
void dirdcl(void);

int tokentype;
char token[MAXTOKEN];
char name[MAXTOKEN];
char datatype[MAXTOKEN];
char out[1000];

int gettoken(void) {
	int c;
	char *p = token;
	while ((c = getch()) == ' ' || c == '\t');

	if (c == '(') {
		if ((c = getch()) == ')') {
			strcpy(token, "()");
			return tokentype = PARENS;
		} else {
			ungetch(c);
			return tokentype = '(';
		}
	} else if (c == '[') {
		for (*p++ = c; (*p++ = getch()) != ']';);
		*p = '\0';
		return tokentype = BRACKETS;
	} else if (isalpha(c)) {
		for (*p++ = c; isalnum(c = getch());) *p++ = c;
		*p = '\0';
		ungetch(c);
		return tokentype = NAME;
	} else {
		return tokentype = c;
	}
}
/* dcl:  parse a declarator */
void dcl(void) {
	int ns;
	for (ns = 0; gettoken() == '*'; ) ns++; /* count *'s */
	dirdcl();
	while (ns-- > 0) strcat(out, " pointer to");
}
/* dirdcl:  parse a direct declarator */
void dirdcl(void) {
	if (tokentype == '(') { /* ( dcl ) */
		dcl();
		if (tokentype != ')')
			printf("error: missing )\n");
	} else if (tokentype == NAME) { /* variable name */
		strcpy(name, token);
	} else {
		printf("error: expected name or (dcl)\n");
	}

	int type;
	while ((type=gettoken()) == PARENS || type == BRACKETS) {
		if (type == PARENS) {
			strcat(out, " function returning");
		} else {
			strcat(out, " array");
			strcat(out, token);
			strcat(out, " of");
		}
	}
}
void part_5_12() {
	init_fake_input_to("char **argv\nint (*daytab)[13]\nint *daytab[13]\nvoid (*comp)()\nchar (*(*x())[])()\n");
//	init_fake_input_to("char (*x)()\n");

	while (gettoken() != EOF) {  /* 1st token on line is the datatype */
		strcpy(datatype, token);
		out[0] = '\0';
		dcl();                   /* parse rest of line */
		if (tokentype != '\n')
			printf("syntax error\n");
		printf("%s: %s %s\n", name, out, datatype);
	}
}

struct point {
	int x;
	int y;
};
struct rect {
	struct point pt1;
	struct point pt2;
};
struct point makepoint(int x, int y) {
	struct point temp;
	temp.x = x;
	temp.y = y;
	return temp; // seems that local struct is "copied" on method return
}
struct point addpoint(struct point p1, struct point p2) {
	p1.x += p2.x; // this doesn't modify point outside of this method, because it's all passed by-value and here is local copy
	p1.y += p2.y;
	return p1;
}
int isPointInRect(struct point p, struct rect r) {
	return p.x >= r.pt1.x && p.x < r.pt2.x
		&& p.y >= r.pt1.y && p.y < r.pt2.y;
}
void part_6_2() {
	struct point pt;
	struct rect screen;

    printf("pt: %d,%d\n", pt.x, pt.y);

	screen.pt1 = makepoint(0, 0);
	screen.pt2 = makepoint(1234, 1234);

	struct point middle = makepoint((screen.pt1.x + screen.pt2.x) / 2, (screen.pt1.y + screen.pt2.y) / 2);
	printf("middle: %d,%d\n", middle.x, middle.y);

	struct point newMiddle = addpoint(middle, makepoint(10, 10));
	printf("middle: %d,%d\n", middle.x, middle.y);
	printf("newMiddle: %d,%d\n", newMiddle.x, newMiddle.y);

	printf("is within screen: %s\n", isPointInRect(makepoint(100, 100), screen) == 0 ? "false" : "true");
	printf("is within screen: %s\n", isPointInRect(makepoint(2000, 100), screen) == 0 ? "false" : "true");

	struct point origin = makepoint(111, 222);
	struct point *pp = &origin;
    printf("origin is (%d,%d)\n", (*pp).x, (*pp).y);
    printf("origin is (%d,%d)\n", pp->x, pp->y);
}

struct key {
	char *word;
	int count;
};
/* getword:  get next word or character from input */
int getword(char *word, int lim) {
	int c;

	char *w = word;
	while (isspace(c = getch()));
	if (c != EOF) *w++ = c;
	if (!isalpha(c)) {
		*w = '\0';
		return c;
	}
	for (; --lim > 0; w++)
		if (!isalnum(*w = getch())) {
			ungetch(*w);
			break;
		}
	*w = '\0';
	return word[0];
}
/* binsearch:  find word in tab[0]...tab[n-1] */
int binsearch(char *word, struct key tab[], int n) {
	int cond;
	int low, high, mid;
	low = 0;
	high = n - 1;
	while (low <= high) {
		mid = (low + high) / 2;
		if ((cond = strcmp(word, tab[mid].word)) < 0)
			high = mid - 1;
		else if (cond > 0)
			low = mid + 1;
		else
			return mid;
	}
	return -1;
}

void part_6_3() {
	init_fake_input_to("const aaa;\nvoid void;");

	struct key keytab[] = {
		"auto", 0,
		"break", 0,
		"case", 0,
		"char", 0,
		"const", 0,
		"continue", 0,
		"default", 0,
		/* ... */
		"unsigned", 0,
		"void", 0,
		"volatile", 0,
		"while", 0
	};
	#define MAXWORD 100
	#define NKEYS (sizeof keytab / sizeof(keytab[0]))

	int n;
	char word[MAXWORD];
	while (getword(word, MAXWORD) != EOF)
		if (isalpha(word[0]))
			if ((n = binsearch(word, keytab, NKEYS)) >= 0)
				keytab[n].count++;
	for (n = 0; n < NKEYS; n++)
		if (keytab[n].count > 0)
			printf("%4d %s\n", keytab[n].count, keytab[n].word);
	printf("\n");
}

struct key *binsearch_6_4(char *word, struct key *tab, int n) {
	int cond;
	struct key *low = &tab[0];
	struct key *high = &tab[n];
	struct key *mid;
	while (low < high) {
		mid = low + (high - low) / 2; // (low+high) / 2 is illegal pointer operation
		if ((cond = strcmp(word, mid->word)) < 0)
			high = mid;
		else if (cond > 0)
			low = mid + 1;
		else
			return mid;
	}
	return NULL;
}
void part_6_4() {
	init_fake_input_to("volatile void;\n auto auto; !!\n");

	struct key keytab[] = {
		"auto", 0,
		"break", 0,
		"case", 0,
		"char", 0,
		"const", 0,
		"continue", 0,
		"default", 0,
		/* ... */
		"unsigned", 0,
		"void", 0,
		"volatile", 0,
		"while", 0
	};
	#define MAXWORD 100
    #define NKEYS (sizeof keytab / sizeof(keytab[0]))

	char word[MAXWORD];
	struct key *p;

	while (getword(word, MAXWORD) != EOF)
		if (isalpha(word[0]))
			if ((p = binsearch_6_4(word, keytab, NKEYS)) != NULL)
				p->count++;
	for (p = keytab; p < keytab + NKEYS; p++)
		if (p->count > 0)
			printf("%4d %s\n", p->count, p->word);
	printf("\n");
}

struct tnode {
    char *word;
	int count;
	struct tnode *left;
	struct tnode *right;
};
/* talloc:  make a tnode */
struct tnode *talloc(void) {
	return (struct tnode *) malloc(sizeof(struct tnode));
}
/* make a duplicate of s */
char *strdup_6_5(char *s) {
	char *p;
	p = (char *) malloc(strlen(s)+1); /* +1 for ’\0’ */
	if (p != NULL) strcpy(p, s);
	return p;
}
/* addtree:  add a node with w, at or below p */
struct tnode *addtree(struct tnode *p, char *w) {
	int cond;
	if (p == NULL) {     /* a new word has arrived */
		p = talloc();    /* make a new node */
		p->word = strdup_6_5(w);
		p->count = 1;
		p->left = p->right = NULL;
	} else if ((cond = strcmp(w, p->word)) == 0) {
		p->count++;      /* repeated word */
	} else if (cond < 0) {   /* less than into left subtree */
		p->left = addtree(p->left, w);
	} else {            /* greater than into right subtree */
		p->right = addtree(p->right, w);
	}
	return p;
}
/* treeprint:  in-order print of tree p */
void treeprint(struct tnode *p) {
	if (p != NULL) {
		treeprint(p->left);
		printf("%4d %s\n", p->count, p->word);
		treeprint(p->right);
	}
}
void part_6_5() {
	init_fake_input_to("now is the time for all good men to come to the aid of their party... now now now");

	#define MAXWORD 100

	char word[MAXWORD];
	struct tnode *root = NULL;
	while (getword(word, MAXWORD) != EOF)
		if (isalpha(word[0]))
			root = addtree(root, word);
	treeprint(root);
}


struct nlist {
	struct nlist *next;
	char *name;
	char *defn;
};
#define HASHSIZE 101
static struct nlist *hashtab[HASHSIZE];

unsigned hash(char *s) {
	unsigned hashval;
	for (hashval = 0; *s != '\0'; s++)
		hashval = *s + 31 * hashval;
	return hashval % HASHSIZE;
}
/* lookup:  look for s in hashtab */
struct nlist *lookup(char *s) {
	struct nlist *np;
	for (np = hashtab[hash(s)]; np != NULL; np = np->next)
		if (strcmp(s, np->name) == 0)
			return np;     /* found */
	return NULL;           /* not found */
}
/* install:  put (name, defn) in hashtab */
struct nlist *install(char *name, char *defn) {
	struct nlist *np;
	unsigned hashval;
	if ((np = lookup(name)) == NULL) { /* not found */
		np = (struct nlist *) malloc(sizeof(*np));
		if (np == NULL || (np->name = strdup(name)) == NULL)
			return NULL;
		hashval = hash(name);
		np->next = hashtab[hashval];
		hashtab[hashval] = np;
	} else       /* already there */
		free((void *) np->defn);   /*free previous defn */
	if ((np->defn = strdup(defn)) == NULL)
		return NULL;
	return np;
}
void part_6_6() {
    struct nlist *result;

    result = lookup("MY_VAR");
    printf("MY_VAR: %s\n", result == NULL ? "null" : result->defn);

    result = install("MY_VAR", "a value");
    printf("MY_VAR: %s\n", result == NULL ? "null" : result->defn);

    result = install("MY_VAR", "a value 2");
    printf("MY_VAR: %s\n", result == NULL ? "null" : result->defn);
}

struct {
	unsigned int is_keyword : 1;
	unsigned int is_extern  : 1;
	unsigned int is_static  : 1;
} flags;
void part_6_7() {
	flags.is_extern = flags.is_static = 1; // turn bits on  (instead of "flags |= EXTERNAL | STATIC;")
	flags.is_extern = flags.is_static = 0; // turn bits off (instead of "flags &= ~(EXTERNAL | STATIC);")
	if (flags.is_extern == 0 && flags.is_static == 0) // instead of "if ((flags & (EXTERNAL | STATIC)) == 0)"
		printf("not extern and not static\n");
}

void resetDate(int *day, int *month, char monthname[], int *year) {
	*day = 0;
	*month = 0;
	monthname[0] = '-';
	monthname[1] = '\0';
	*year = 0;
}

void part_7_4() {
    int day, month, year;
    char monthname[20];

    resetDate(&day, &month, monthname, &year);
    sscanf("", "%d %s %d", &day, monthname, &year);
    printf("day: %d; month: %d (%s); year %d\n", day, month, monthname, year);

    resetDate(&day, &month, monthname, &year);
    sscanf("25 Dec 1988", "%d %s %d", &day, monthname, &year);
    printf("day: %d; month: %d (%s); year %d\n", day, month, monthname, year);

    resetDate(&day, &month, monthname, &year);
    sscanf("21 01 1989", "%d %d %d", &day, &month, &year);
    printf("day: %d; month: %d (%s); year %d\n", day, month, monthname, year);
}

void part_7_5() {
	FILE *fp;
	fp = fopen("/Users/dima/IdeaProjects/katas/src/main/c/hello_read.txt", "r");
	if (fp == NULL) {
		printf("Failed to open file\n");
		return;
	}

	char c;
	while ((c = getc(fp)) != EOF) {
		printf("%c", c);
	}

	int result;
	result = fclose(fp);
	if (result == EOF) {
		printf("Failed to close file\n");
	}
}

typedef long Align;    /* for alignment to long boundary */
union header {         /* block header */
	struct {
		union header *ptr; /* next block if on free list */
		unsigned size;     /* size of this block */
	} s;
	Align x;           /* force alignment of blocks */
};
typedef union header Header;

static Header base; /* empty list to get started */
static Header *freep = NULL; /* start of free list */

#define NALLOC  1024   /* minimum #units to request */

/* free:  put block ap in free list */
void free_(void *ap) {
	Header *bp, *p;

	bp = (Header *)ap - 1; /* point to  block header */
	for (p = freep; !(bp > p && bp < p->s.ptr); p = p->s.ptr)
		if (p >= p->s.ptr && (bp > p || bp < p->s.ptr))
			break; /* freed block at start or end of arena */

	if (bp + bp->s.size == p->s.ptr) { /* join to upper nbr */
		bp->s.size += p->s.ptr->s.size;
		bp->s.ptr = p->s.ptr->s.ptr;
	} else {
		bp->s.ptr = p->s.ptr;
	}

	if (p + p->s.size == bp) { /* join to lower nbr */
		p->s.size += bp->s.size;
		p->s.ptr = bp->s.ptr;
	} else {
		p->s.ptr = bp;
	}
	freep = p;
}

/* morecore:  ask system for more memory */
static Header *morecore(unsigned nu) {
	char *cp, *sbrk(int);
	Header *up;

	if (nu < NALLOC) nu = NALLOC;

	cp = sbrk(nu * sizeof(Header));
	if (cp == (char *) -1) /* no space at all */
		return NULL;

	up = (Header *) cp;
	up->s.size = nu;
	free_((void *)(up+1));
	return freep;
}

/* malloc:  general-purpose storage allocator */
void *malloc_(unsigned nbytes) {
	Header *p, *prevp;
	Header *moreroce(unsigned);
	unsigned nunits;

	nunits = (nbytes+sizeof(Header)-1)/sizeof(Header) + 1;
	if ((prevp = freep) == NULL) {   /* no free list yet */
		base.s.ptr = freep = prevp = &base;
		base.s.size = 0;
	}
	for (p = prevp->s.ptr; ; prevp = p, p = p->s.ptr) {
		if (p->s.size >= nunits) {  /* big enough */
			if (p->s.size == nunits) { /* exactly */
				prevp->s.ptr = p->s.ptr;
			} else {              /* allocate tail end */
				p->s.size -= nunits;
				p += p->s.size;
				p->s.size = nunits;
			}
			freep = prevp;
			return (void *)(p+1);
		}
		if (p == freep)  /* wrapped around free list */
			if ((p = morecore(nunits)) == NULL)
				return NULL;    /* none left */
	}
}

void part_8_7() {
	void *p = malloc_(5000);
	free_(p);
}


int main() {
// TODO try using /usr/local/Cellar/check/0.9.8

//    part_1_1();
//    part_1_2();
//    part_1_5_1();
//    part_1_5_2();
//    part_1_5_3();
//    part_1_5_4();
//    part_1_6();
//    part_1_7();
//    part_1_8();
//
//    part_2_2();
//    part_2_3();
//    part_2_7();

//    part_4_3();
//    part_4_6();
//    part_4_10();
//    part_4_11();

//    part_5_1();
//    part_5_2();
//    part_5_4();
//    part_5_5();
//    part_5_6();
//    part_5_7();
//    part_5_8();
//    part_5_11();
//    part_5_12();

//    part_6_2();
//    part_6_3();
//    part_6_4();
//    part_6_5();
//    part_6_6();
//    part_6_7();

//    part_7_4();
//    part_7_5();

	part_8_7();
}