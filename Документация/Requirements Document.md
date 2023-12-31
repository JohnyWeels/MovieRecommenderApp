# Requirements Document📝



## 1.	ВВЕДЕНИЕ

**Название продукта:** "Movie Recommender App";
**Краткое описание:** Приложение предоставляет рекомендации по трем фильмам каждый день. Пользователи могут просматривать информацию о фильмах, включая название, обложку, краткое описание, трейлер и ссылку на просмотр. Приложение помогает пользователям открывать для себя новые фильмы и разнообразить свой киновкус.

## 2.	ТРЕБОВАНИЯ ПОЛЬЗОВАТЕЛЯ

#### 2.1 Программные интерфейсы

Продукт будет взаимодействовать с внешними сервисами для получения информации о фильмах, такими как API для кинофильмов и изображений:
-	API для фильмов:  приложение будет использовать API кинопоиска или подобные, чтобы получать информацию о фильмах, такую как название, описание, обложки, трейлеры и ссылки для просмотра;
-	API для изображений: для загрузки обложек фильмов приложение может использовать специальные API для работы с изображениями или сервисы для хранения и предоставления изображений;
-	API для видео и трейлеров: для воспроизведения трейлеров фильмов, необходимо интегрировать API для видеоплеера, такие как YouTube API;
-	База данных: использование баз данных (SQL или NoSQL) для хранения информации о пользователях и их предпочтениях;
-	Интернет-сервисы: для аутентификации, авторизации и обновления данных о фильмах, приложение может взаимодействовать с интернет-сервисами через HTTP-запросы.

#### 2.2 Интерфейс пользователя

Для обеспечения удобства пользователей и понимания того, как они будут взаимодействовать с приложением, интерфейс пользователя может включать следующие элементы:
-	Название фильма:  название фильма будет отображено в верхней части окна приложения, чтобы пользователь сразу увидел, о каком фильме идет речь;
-	Обложка фильма: слева от названия фильма будет располагаться изображение обложки фильма. Обложка может быть кликабельной, чтобы пользователи могли увидеть изображение в увеличенном виде или открыть дополнительную информацию о фильме;
-	Краткое описание: под названием фильма будет текстовое поле, отображающее краткое описание фильма. Это описание предоставляет пользователю общую информацию о сюжете фильма и его ключевых особенностях;
-	Трейлер: для увеличения привлекательности приложения, можно включить видеоплеер для просмотра трейлера фильма. Пользователи могут просмотреть краткий видеоролик и решить, заинтересованы ли они в данном фильме;
-	Ссылка на просмотр: под описанием фильма, можно включить ссылку, которая направляет пользователя на веб-сайт или платформу для просмотра фильма. Пользователи могут перейти по этой ссылке, чтобы смотреть фильм в полной версии;
-	Кнопка "Обновить": для обновления рекомендаций, необходимо добавить кнопку "Обновить" в нижней части окна приложения. Пользователи могут нажать на эту кнопку, чтобы получить новые рекомендации на основе своих предпочтений или текущего дня.

Общий интерфейс должен быть интуитивно понятным и привлекательным, чтобы пользователи могли легко понимать, как использовать приложение для получения информации о фильмах и просмотра трейлеров. Информация о фильмах должна быть представлена в удобном и читаемом формате, а кнопки и ссылки должны быть хорошо видимыми и доступными.

#### 2.3 Характеристики пользователей

Для лучшего понимания аудитории пользователей приложения и разработки интерфейса нужно учитывать потребности стандартного пользователя, а именно предположить группы пользователей и их примерные характеристики:
-	Пользователи приложения – это обычные пользователи, любители кино всех возрастов и категорий. Это могут быть как подростки, так и взрослые, разного пола и интересов. 
-	Характеристики пользователей учитывают уровень образования, опыт, техническую грамотность: аудитория может быть разной степени образования, от студентов до специалистов с высшим образованием. Приложение должно предоставлять информацию на доступном уровне и, при необходимости, предоставлять дополнительные детали для более образованных пользователей. Пользователи также могут иметь разный опыт в использовании подобных приложений. Некоторые могут быть опытными, а другие – новичками. Одним из главных факторов является то, что пользователи могут иметь разный опыт технической грамотности. Приложение должно иметь интуитивно понятным и лёгким в использовании для всех, независимо от технического опыта.

Таким образом, рассмотрение этих характеристик поможет создать пользовательский опыт, который будет учитывать разнообразные потребности и ожидания пользователей, обеспечивая при этом удовлетворительное взаимодействие с приложением.

#### 2.4 Предположения и зависимости

Качество рекомендаций и работоспособность приложения могут зависить от следующих факторов:
-	Доступность внешних источников данных: приложение зависит от внешних источников данных о фильмах. Предполагается, что эти источники данных будут доступны и обновляться регулярно. Ошибки или задержки в доступности данных могут повлиять на возможность предоставления рекомендаций;
-	Доступ к интернету: поскольку приложение будет загружать данные о фильмах и, возможно, видео, предполагается, что пользователи будут иметь доступ к интернету. Отсутствие интернет-соединения может ограничить функциональность приложения;
-	Стабильность сторонних сервисов и API:  при использовании сторонних сервисов и API, таких как API для фильмов и изображений, предполагается, что эти сервисы будут стабильными и доступными. Изменения в структуре данных или недоступность API могут потребовать обновлений в приложении;
-	Поддержка платформы Java: приложение разрабатывается на платформе Java, поэтому предполагается, что целевая аудитория сможет запускать приложение на совместимых устройствах и системах;
-	Операционная система:  в зависимости от того, на какой операционной системе будет выполняться приложение, возможны различия в работе и интерфейсе. Важно учесть требования разных операционных систем.

## 3.	СИСТЕМНЫЕ ТРЕБОВАНИЯ

#### 3.1 Функциональные требования

Функциональные требования описывают, что ваше приложение должно делать, какие функции оно должно предоставлять:
-	Отображение информации о фильмах:  приложение должно отображать информацию о трех рекомендованных фильмах, включая название, обложку, краткое описание, трейлер и ссылку на просмотр;
-	Обновление рекомендаций:  пользователи должны иметь возможность обновить список рекомендаций. При нажатии кнопки "Обновить", приложение должно загрузить новые рекомендации на основе предпочтений пользователя или других параметров;
-	Просмотр трейлеров: пользователи должны иметь возможность просматривать трейлеры фильмов непосредственно в приложении. Трейлеры должны воспроизводиться внутри интерфейса;
-	Ссылки на просмотр:  приложение должно предоставлять активные ссылки на внешние веб-сайты или платформы для просмотра фильмов, если они доступны.

#### 3.2 Нефункциональные требования

Нефункциональные требования определяют атрибуты качества и ограничения, которые должны соблюдаться приложением:
-	Надёжность: приложение должно быть стабильным и надежным, минимизируя возможные сбои и ошибки. Рекомендации должны загружаться без проблем, даже при временной недоступности внешних источников данных;
-	Безопасность: при сборе и хранении личных данных пользователей (если таковые собираются), приложение должно обеспечивать их безопасность. Это включает в себя шифрование данных и защиту от несанкционированного доступа;
-	Производительность: приложение должно быть эффективным и быстрым в загрузке данных и обновлении интерфейса. Задержки и перегрузки должны быть минимальными.

