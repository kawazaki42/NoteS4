% % Данные: комплексные значения (например, напряжения или токи)
% Z = [
% %    1 + 1.5i;    % вектор 1
% %    2 - 1i;      % вектор 2
% %    -1.5 - 0.5i; % вектор 3
% %    0.5 + 2i     % вектор 4
%     (22.814 + 26.594j) / 10;
%     (-0.2771-0.9293j) * 10;
% ];
% 
% % Начало всех векторов (можно задать общее или разные)
% start_x = zeros(size(Z));
% start_y = zeros(size(Z));
% 
% % Конечные точки
% end_x = real(Z);
% end_y = imag(Z);
% 
% % Построить стрелки
% figure;
% % quiver(start_x, start_y, end_x, end_y, 0, 'LineWidth', 2, 'MaxHeadSize', 0.5);
% quiver(start_x, start_y, end_x, end_y, 0, 'LineWidth', 2);
% % axis equal;
% grid on;
% hold on;
% 
% % Подписи у концов векторов
% %for k = 1:length(Z)
% %    text(real(Z(k)), imag(Z(k)), sprintf('  v%d', k), 'FontSize', 12);
% %end
% 
% test(real(Z(1)), imag(Z(1)), "U_ab")
% 
% % Оси и оформление
% xlabel('Re');
% ylabel('Im');
% % title('Векторная диаграмма');
% line([-3 3], [0 0], 'Color', 'k', 'LineStyle', ':'); % ось Re
% line([0 0], [-3 3], 'Color', 'k', 'LineStyle', ':'); % ось Im
% % axis([-3 3 -3 3]);
% hold off;

function draw_angle(v, radius, start_angle_rad, end_angle_rad, label)
    % v - центр дуги (обычно [0,0])
    % radius - радиус дуги
    % start_angle_rad, end_angle_rad - пределы в радианах
    % label - текст с углом (например, "45°")
    theta = linspace(start_angle_rad, end_angle_rad, 50);
    x = v(1) + radius * cos(theta);
    y = v(2) + radius * sin(theta);
    plot(x, y, 'r-', 'LineWidth', 1.5);
    % Положение текста - посередине дуги
    mid_angle = (start_angle_rad + end_angle_rad)/2;
    tx = v(1) + radius * 1.2 * cos(mid_angle);
    ty = v(2) + radius * 1.2 * sin(mid_angle);
    text(tx, ty, label, 'FontSize', 10, 'Color', 'red');
end

hold on;

% Пример комплексного числа
Z = 1 + 1.5i;
angle_rad = arg(Z);          % радианы
angle_deg = angle_rad * 180/pi;

% Построение вектора
figure;
quiver(0, 0, real(Z), imag(Z), 0, 'LineWidth', 2);
axis equal; grid on; hold on;
xlabel('Re'); ylabel('Im');
line([-2 2], [0 0], 'Color', 'k', 'LineStyle', ':');
line([0 0], [-2 2], 'Color', 'k', 'LineStyle', ':');

% Рисуем дугу от 0 до угла вектора
draw_angle([0,0], 0.6, 0, angle_rad, sprintf('%.0f°', angle_deg));

% Подпись вектора
text(real(Z), imag(Z), sprintf('  Z = %.1f∠%.0f°', abs(Z), angle_deg));
hold off;
