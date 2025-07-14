document.addEventListener('DOMContentLoaded', function() {
    const navItems = document.querySelectorAll('.main-nav .nav-item');
    const commonMegaMenu = document.querySelector('.common-mega-menu-dropdown');
    const megaMenuContents = document.querySelectorAll('.mega-menu-content'); // 모든 콘텐츠 요소를 가져옴
    const overlay = document.querySelector('.overlay');
    const mainHeader = document.querySelector('.main-header'); // 헤더 전체

    let activeNavLink = null; // 현재 활성화된 nav link
    let activeContentId = null; // 현재 활성화된 콘텐츠 ID

    function closeMegaMenu() {
        mainHeader.classList.remove('menu-open'); // 헤더에 클래스 제거
        commonMegaMenu.style.opacity = '0';
        commonMegaMenu.style.visibility = 'hidden';
        commonMegaMenu.style.transform = 'translateY(-20px)';
        overlay.classList.remove('show');

        // 모든 메뉴 콘텐츠 비활성화
        megaMenuContents.forEach(content => {
            content.classList.remove('active');
        });

        // 활성화된 nav link 스타일 제거
        if (activeNavLink) {
            activeNavLink.closest('.nav-item').classList.remove('active');
            activeNavLink = null;
        }
        activeContentId = null; // 활성화된 콘텐츠 ID 초기화
    }

    navItems.forEach(item => {
        const navLink = item.querySelector('.nav-link');
        const targetId = navLink.getAttribute('data-target'); // 클릭될 때 보여줄 콘텐츠의 ID

        if (navLink) { // targetContent는 클릭시에만 찾으면 됨
            navLink.addEventListener('click', function(e) {
                e.preventDefault(); // 기본 링크 동작 방지

                // 현재 클릭된 메뉴가 이미 활성화된 메뉴라면 닫기
                if (item.classList.contains('active')) {
                    closeMegaMenu();
                } else {
                    // 모든 nav-item의 active 클래스 제거
                    navItems.forEach(ni => ni.classList.remove('active'));
                    // 모든 mega-menu-content의 active 클래스 제거 (이 부분이 핵심!)
                    megaMenuContents.forEach(mc => mc.classList.remove('active'));

                    // 클릭된 nav-item에 active 클래스 추가
                    item.classList.add('active');
                    activeNavLink = navLink; // 현재 활성화된 링크 저장
                    activeContentId = targetId; // 현재 활성화된 콘텐츠 ID 저장

                    // 해당하는 mega-menu-content만 활성화 (display: flex로 변경)
                    const targetContent = document.getElementById(targetId);
                    if (targetContent) {
                        targetContent.classList.add('active');
                    }

                    // 공통 메가 메뉴 영역 보이게 하기
                    mainHeader.classList.add('menu-open'); // 헤더에 클래스 추가
                    commonMegaMenu.style.display = 'block'; // 'block'으로 설정
                    commonMegaMenu.style.opacity = '1';
                    commonMegaMenu.style.visibility = 'visible';
                    commonMegaMenu.style.transform = 'translateY(0)';
                    overlay.classList.add('show');
                }
            });
        }
    });

    // 오버레이 클릭 시 메뉴 닫기
    overlay.addEventListener('click', closeMegaMenu);

    // ESC 키 눌렀을 때 메뉴 닫기
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeMegaMenu();
        }
    });

    // 윈도우 크기 변경 시 메뉴 닫기 (반응형 대응)
    window.addEventListener('resize', closeMegaMenu);

    // common-mega-menu-dropdown이 완전히 닫힌 후 display: none 처리
    commonMegaMenu.addEventListener('transitionend', function() {
        if (commonMegaMenu.style.opacity === '0') {
            commonMegaMenu.style.display = 'none';
        }
    });
    
    // 문서의 아무 곳이나 클릭했을 때 메뉴 닫기 기능 추가 (수정됨)
    document.addEventListener('click', function(e) {
        // 1. 클릭된 요소가 common-mega-menu-dropdown 안에 있는지 확인
        const isClickInsideMegaMenu = commonMegaMenu.contains(e.target);

        // 2. 클릭된 요소가 nav-item (nav-link 포함) 안에 있는지 확인
        let isClickInsideNavItem = false;
        navItems.forEach(item => {
            if (item.contains(e.target)) {
                isClickInsideNavItem = true;
            }
        });

        // 3. 현재 메뉴가 열려있고, 클릭된 위치가 메가 메뉴도 아니고, 내비게이션 아이템도 아니라면 메뉴를 닫습니다.
        if (mainHeader.classList.contains('menu-open') && !isClickInsideMegaMenu && !isClickInsideNavItem) {
            closeMegaMenu();
        }
    });
    
    // 계정 아이콘
    $("#profileIcon").click(function(event) {
        $("#profilePopup").toggleClass("hidden");
        event.stopPropagation();
    });

    $(document).click(function() {
        $("#profilePopup").addClass("hidden");
    });

    $("#profilePopup").click(function(event) {
        event.stopPropagation();
    });
    
   
});

